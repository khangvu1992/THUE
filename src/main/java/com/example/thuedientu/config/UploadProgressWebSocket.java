package com.example.thuedientu.config;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/uploadProgress")
public class UploadProgressWebSocket {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket connection opened.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from client: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket connection closed.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // Gửi tiến độ lên WebSocket
    public void sendProgress(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }
}
