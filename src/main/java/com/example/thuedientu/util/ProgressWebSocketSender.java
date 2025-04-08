package com.example.thuedientu.util;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProgressWebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public ProgressWebSocketSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendProgress(String fileId, String fileName, int processed, int totalBatches, boolean done, String error) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("fileId", fileId);
        payload.put("fileName", fileName);
        payload.put("processed", processed);
        payload.put("queueSize", totalBatches);
        payload.put("done", done);
        payload.put("error", error);

        messagingTemplate.convertAndSend("/topic/progress", payload);
    }

    public void sendProgress1(String khang) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("fileId", khang);


        messagingTemplate.convertAndSend("/topic/progress", payload);
    }
}
