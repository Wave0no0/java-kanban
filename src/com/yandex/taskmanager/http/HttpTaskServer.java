package com.yandex.taskmanager.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.yandex.taskmanager.client.GsonUtils;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.manager.Managers;
import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;
import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.Status;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private final TaskManager taskManager;
    private HttpServer server;

    private final Gson gson;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;

        // Регистрируем кастомные сериализаторы и десериализаторы
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new GsonUtils.DurationSerializer())
                .registerTypeAdapter(Duration.class, new GsonUtils.DurationDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new GsonUtils.LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new GsonUtils.LocalDateTimeDeserializer())
                .create();
    }

    public void start() {
        try {
            // Наполняем менеджер задачами
            initializeData();

            // Создаем сервер и добавляем обработчики
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/tasks", new TaskHandler(taskManager, gson));
            server.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
            server.createContext("/epics", new EpicHandler(taskManager, gson));
            server.createContext("/history", new HistoryHandler(taskManager, gson));
            server.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
            server.setExecutor(null); // создает стандартный исполнитель
            server.start();
            System.out.println("Server started on port 8080");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    private void initializeData() {
        try {
            // Пример задач
            Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW,
                    Duration.ofHours(1), LocalDateTime.of(2024, 12, 1, 10, 0));
            Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS,
                    Duration.ofHours(2), LocalDateTime.of(2024, 12, 2, 11, 0));
            taskManager.addTask(task1);
            taskManager.addTask(task2);

            // Пример эпика и подзадач
            Epic epic = new Epic(3, "Epic 1", "Description Epic", Status.NEW);
            taskManager.addEpic(epic);

            Subtask subtask1 = new Subtask(4, "Subtask 1", "Description Subtask 1", Status.NEW,
                    epic.getId(), Duration.ofHours(1), LocalDateTime.of(2024, 12, 3, 9, 0));
            Subtask subtask2 = new Subtask(5, "Subtask 2", "Description Subtask 2", Status.DONE,
                    epic.getId(), Duration.ofHours(2), LocalDateTime.of(2024, 12, 4, 14, 0));
            taskManager.addSubtask(subtask1);
            taskManager.addSubtask(subtask2);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault(); // Используем InMemoryTaskManager
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}
