package com.example.thuedientu.util;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.*;

@Component
public class DownloadManager {

    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final Set<String> runningTasks = ConcurrentHashMap.newKeySet();

    private final int MAX_CONCURRENT_TASKS = 12; // Số worker tối đa đồng thời
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_CONCURRENT_TASKS);

    @PostConstruct
    public void init() {
        // Thread riêng lắng nghe hàng đợi
        Thread dispatcher = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = taskQueue.take(); // Blocking
                    executorService.submit(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "download-dispatcher");

        dispatcher.setDaemon(true);
        dispatcher.start();
    }

    public void enqueue(String fileId, Runnable task) {
        if (isRunning(fileId)) {
            System.out.println("⚠️ File đang được xử lý: " + fileId);
            return;
        }

        System.out.println("📥 Thêm vào hàng đợi: " + fileId);
        markRunning(fileId);
        taskQueue.offer(() -> {
            try {
                task.run();
            } finally {
                markDone(fileId);
            }
        });
    }

    public List<String> getWaitingQueue() {
        List<String> waiting = new ArrayList<>();
        for (Runnable r : taskQueue) {
            waiting.add(r.toString()); // Optional: cần override toString trong task nếu muốn rõ fileId
        }
        return waiting;
    }

    public void markRunning(String fileId) {
        runningTasks.add(fileId);
    }

    public void markDone(String fileId) {
        runningTasks.remove(fileId);
        System.out.println("✅ Hoàn tất xử lý file: " + fileId);
    }

    public boolean isRunning(String fileId) {
        return runningTasks.contains(fileId);
    }

    public boolean isIdle() {
        return taskQueue.isEmpty() && runningTasks.isEmpty();
    }
}
