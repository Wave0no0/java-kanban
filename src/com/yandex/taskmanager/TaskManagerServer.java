package com.yandex.taskmanager;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpContext;
import com.yandex.taskmanager.http.EpicHandler;
import com.yandex.taskmanager.http.SubtaskHandler;
import com.yandex.taskmanager.http.TaskHandler;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.manager.Managers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;

public class TaskManagerServer {
    private final HttpServer server;
    private final TaskManager taskManager;
    private final Gson gson; // Объект Gson

    public TaskManagerServer(int port) throws IOException {
        this.taskManager = Managers.getDefault(); // Создаем экземпляр менеджера задач
        this.gson = new GsonBuilder()
                .registerTypeAdapter(java.time.Duration.class, new com.yandex.taskmanager.client.GsonUtils.DurationSerializer())
                .registerTypeAdapter(java.time.Duration.class, new com.yandex.taskmanager.client.GsonUtils.DurationDeserializer())
                .registerTypeAdapter(java.time.LocalDateTime.class, new com.yandex.taskmanager.client.GsonUtils.LocalDateTimeSerializer())
                .registerTypeAdapter(java.time.LocalDateTime.class, new com.yandex.taskmanager.client.GsonUtils.LocalDateTimeDeserializer())
                .create(); // Инициализация Gson с кастомными сериализаторами

        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        initializeHandlers();
    }

    private void initializeHandlers() {
        // Создаем и передаем gson в обработчики
        HttpContext taskContext = server.createContext("/tasks");
        taskContext.setHandler(new TaskHandler(taskManager, gson));

        HttpContext subtaskContext = server.createContext("/subtasks");
        subtaskContext.setHandler(new SubtaskHandler(taskManager, gson));

        HttpContext epicContext = server.createContext("/epics");
        epicContext.setHandler(new EpicHandler(taskManager, gson));
    }

    public void start() {
        server.start();
        System.out.println("Server is running on port " + server.getAddress().getPort());
    }

    public static void main(String[] args) {
        try {
            TaskManagerServer server = new TaskManagerServer(8080); // Укажите желаемый порт
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
