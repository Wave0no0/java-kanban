package com.yandex.taskmanager.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.task.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Task> history = taskManager.getHistory();
            String response = gson.toJson(history);
            sendText(exchange, response, 200);
        } else {
            sendNotFound(exchange);
        }
    }
}
