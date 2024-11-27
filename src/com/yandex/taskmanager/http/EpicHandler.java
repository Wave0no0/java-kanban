package com.yandex.taskmanager.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.task.Epic;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        switch (exchange.getRequestMethod()) {
            case "GET":
                response = handleGet(exchange);
                sendText(exchange, response, 200);
                break;
            case "POST":
                response = handlePost(exchange);
                sendText(exchange, response, 201);
                break;
            case "DELETE":
                handleDelete(exchange);
                sendText(exchange, "Epic deleted", 200);
                break;
            default:
                sendNotFound(exchange);
                break;
        }
    }

    private String handleGet(HttpExchange exchange) {
        return gson.toJson(taskManager.getEpics());
    }

    private String handlePost(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        Epic epic = gson.fromJson(body.toString(), Epic.class);
        taskManager.addEpic(epic);
        return gson.toJson(epic);
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        taskManager.deleteEpicByID(id);
    }
}
