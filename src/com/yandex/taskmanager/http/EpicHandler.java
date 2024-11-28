package com.yandex.taskmanager.http;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.task.Epic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler {
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
                if (response != null) {
                    sendText(exchange, response, 201);
                }
                break;
            case "DELETE":
                handleDelete(exchange);
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

        Epic epic;
        try {
            epic = gson.fromJson(body.toString(), Epic.class);
        } catch (Exception e) {
            sendText(exchange, "Invalid Epic JSON format", 400);
            return null;
        }

        if (epic == null || epic.getName() == null || epic.getName().isEmpty()
                || epic.getDescription() == null || epic.getDescription().isEmpty()) {
            sendText(exchange, "Invalid Epic Data: name and description are required", 400);
            return null;
        }

        taskManager.addEpic(epic);
        return gson.toJson(epic);
    }

    @Override
    protected void deleteEntityById(int id) throws IOException {
        taskManager.deleteEpicByID(id);
    }
}
