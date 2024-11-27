package com.yandex.taskmanager.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.task.Task;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        try {
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
                    sendText(exchange, "Task deleted", 200);
                    break;
                default:
                    sendNotFound(exchange);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Логирование ошибки
            sendText(exchange, "Internal Server Error: " + e.getMessage(), 500); // Отправка ошибки
        }
    }

    private String handleGet(HttpExchange exchange) {
        return gson.toJson(taskManager.getTasks());
    }

    private String handlePost(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        String bodyStr = body.toString();
        System.out.println("Received POST request with body: " + bodyStr); // Логирование данных запроса

        if (bodyStr.isEmpty()) {
            sendText(exchange, "Request body is empty", 400); // Ошибка, если тело запроса пустое
            return null;
        }

        Task task = gson.fromJson(bodyStr, Task.class);
        if (task == null || task.getName() == null || task.getDescription() == null) {
            sendText(exchange, "Invalid Task Data", 400); // Ошибка в данных задачи
            return null;
        }

        taskManager.addTask(task);
        return gson.toJson(task);
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        int id = Integer.parseInt(parts[parts.length - 1]);
        taskManager.deleteTaskByID(id);
    }
}
