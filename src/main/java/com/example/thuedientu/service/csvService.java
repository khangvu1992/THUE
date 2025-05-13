package com.example.thuedientu.service;

import com.example.thuedientu.model.HashFile;
import com.example.thuedientu.model.SeawayHouseBillEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public abstract  class csvService {

    private final int BATCH_SIZE = 5000;
    private final int WORKER_COUNT = 4;

    public void import1Datbase1JDBC1(File file, HashFile hashFile) {
        String fileId = hashFile.getFileHash();
        String filename = hashFile.getFilename();
        createTable(); // Tạo bảng nếu chưa có

        BlockingQueue<List<SeawayHouseBillEntity>> queue = new LinkedBlockingQueue<>(100);
        AtomicBoolean readingDone = new AtomicBoolean(false);
        AtomicInteger totalProcessed = new AtomicInteger(0);

        // Khởi tạo các worker
        for (int i = 0; i < WORKER_COUNT; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        List<SeawayHouseBillEntity> batch = queue.poll(5, TimeUnit.SECONDS);
                        if (batch == null) {
                            if (readingDone.get()) break;
                            continue;
                        }

                        insertDataBatch(batch);
                        long processed = totalProcessed.addAndGet(batch.size());

                        if (processed % 10000 == 0) {
                            int  percent = (int)( processed * 100L / 1048576);
                            System.out.println("Progress: " + (percent * 10) + "%");
                            progressWebSocketSenderSendProgress1(fileId, filename, percent, false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " done!");
                progressWebSocketSenderSendProgress1(fileId, filename, 100, false);

            }, "worker-" + i + "-" + filename).start();
        }

        // Đọc CSV bằng Apache Commons CSV
        try (
                Reader reader = new FileReader(file);
                CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())
        ) {
            int count = 0;
            int nullCount = 0;
            List<SeawayHouseBillEntity> batch = new ArrayList<>();
            CSVRecord lastRecord = null;

            for (CSVRecord record : parser) {
                if (record == null || record.size() == 0) {
                    nullCount++;
                    if (nullCount >= 2) {
                        System.out.println("🛑 Gặp 2 dòng null liên tiếp. Dừng đọc.");
                        break;
                    }
                    continue;
                } else {
                    nullCount = 0;
                }

                count++;


                SeawayHouseBillEntity entity = new SeawayHouseBillEntity();
                mapCsvRowToEntity(record, entity); // cần sửa hàm map để nhận CSVRecord
                batch.add(entity);

                // ✅ In số dòng và nội dung dòng cuối cùng
//                System.out.println("📦 Tổng số dòng đã đọc: " + count);
//                System.out.println("🧾 Dòng cuối cùng:");
//                for (int i = 0; i < record.size(); i++) {
//                    System.out.println("Cột " + (i + 1) + ": " + record.get(i));
//                }

                if (batch.size() >= BATCH_SIZE) {
                    queue.put(new ArrayList<>(batch));
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                queue.put(batch);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            readingDone.set(true);
            file.delete();
            fileRepositorySave(hashFile);
            System.out.println("🧹 Đã xoá file tạm: " + file.getAbsolutePath());

        }
    }

    public abstract <T> void insertDataBatch(List<T> batch) ;
    public abstract void createTable();
    public abstract <T> void fileRepositorySave(T entity);
    public abstract <T,U,V,L> void progressWebSocketSenderSendProgress1(T entity,U e2, V e3, L e4);
    public abstract <T,V> void mapCsvRowToEntity(T record, V entity);


}
