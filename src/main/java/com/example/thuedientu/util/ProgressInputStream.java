package com.example.thuedientu.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProgressInputStream extends InputStream {
    private final InputStream inputStream;
    private final long totalBytes;
    private long bytesRead = 0;
    private final ProgressListener listener;

    public ProgressInputStream(InputStream inputStream, long totalBytes, ProgressListener listener) {
        this.inputStream = inputStream;
        this.totalBytes = totalBytes;
        this.listener = listener;
    }

    @Override
    public int read() throws IOException {
        int byteRead = inputStream.read();
        if (byteRead != -1) {
            bytesRead++;
            listener.onProgress(bytesRead, totalBytes);
        }
        return byteRead;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesReadNow = inputStream.read(b, off, len);
        if (bytesReadNow != -1) {
            bytesRead += bytesReadNow;
            listener.onProgress(bytesRead, totalBytes);
        }
        return bytesReadNow;
    }

    public interface ProgressListener {
        void onProgress(long bytesRead, long totalBytes);
    }
}
