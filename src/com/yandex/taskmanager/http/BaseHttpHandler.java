package com.yandex.taskmanager.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public abstract class BaseHttpHandler implements HttpHandler {

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, text.getBytes().length);
        try (var os = exchange.getResponseBody()) {
            os.write(text.getBytes());
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {//
        sendText(exchange, "Resource not found", 404);
    }

    protected Integer extractIdFromPath(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        try {
            return Integer.parseInt(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            sendText(exchange, "Invalid ID in path", 400);
            return null;
        }
    }

    protected abstract void deleteEntityById(int id) throws IOException;

    protected void handleDelete(HttpExchange exchange) throws IOException {
        Integer id = extractIdFromPath(exchange);
        if (id != null) {
            deleteEntityById(id);
            sendText(exchange, "Entity deleted", 200);
        }
    }
}
