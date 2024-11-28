package com.yandex.taskmanager.http;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.task.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
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
        return gson.toJson(taskManager.getSubtasks());
    }

    private String handlePost(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        Subtask subtask;
        try {
            subtask = gson.fromJson(body.toString(), Subtask.class);
        } catch (Exception e) {
            sendText(exchange, "Invalid Subtask JSON format", 400);
            return null;
        }

        if (subtask == null || subtask.getName() == null || subtask.getName().isEmpty()
                || subtask.getDescription() == null || subtask.getDescription().isEmpty()) {
            sendText(exchange, "Invalid Subtask Data: name and description are required", 400);
            return null;
        }

        taskManager.addSubtask(subtask);
        return gson.toJson(subtask);
    }

    @Override
    protected void deleteEntityById(int id) throws IOException {
        taskManager.deleteSubtaskByID(id);
    }
}
