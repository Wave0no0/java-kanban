package com.yandex.taskmanager.http;

import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.manager.InMemoryTaskManager;
import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.Status;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTest {

    private TaskManager taskManager;
    private HttpTaskServer taskServer;
    private Gson gson;

    public HttpTaskManagerTasksTest() throws Exception {
        taskManager = new InMemoryTaskManager(); // Используем InMemoryTaskManager для тестов
        taskServer = new HttpTaskServer(taskManager); // Инициализируем сервер
        gson = new Gson(); // Просто используем Gson напрямую
    }

    @BeforeEach
    public void setUp() {
        taskManager.deleteTasks(); // Удаляем все задачи перед каждым тестом
        taskServer.start(); // Запускаем сервер
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop(); // Останавливаем сервер после каждого теста
    }

    @Test
    public void testGetTasks() throws Exception {
        // Используем конструктор с id
        Task task = new Task(1, "Test Task", "Description of the test task", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 12, 1, 10, 0));
        taskManager.addTask(task); // Добавляем задачу в TaskManager

        // Отправляем GET-запрос для получения списка задач
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем статус-код
        assertEquals(200, response.statusCode(), "Ответ не получен");

        // Проверяем, что в ответе содержится наша задача
        assertTrue(response.body().contains("Test Task"), "В ответе нет ожидаемой задачи");
    }

    @Test
    public void testDeleteTask() throws Exception {
        // Создаем две задачи
        Task task1 = new Task(1, "Test Task 1", "Description of the first test task", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 12, 1, 10, 0));
        Task task2 = new Task(2, "Test Task 2", "Description of the second test task", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 12, 1, 11, 0));

        taskManager.addTask(task1); // Добавляем первую задачу в TaskManager
        taskManager.addTask(task2); // Добавляем вторую задачу в TaskManager

        // Отправляем DELETE-запрос для удаления первой задачи
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверяем статус-код
        assertEquals(200, response.statusCode(), "Задача не была удалена");

        List<Task> tasks = taskManager.getTasks();
        assertEquals(3, tasks.size(), "Количество задач должно быть 3, но было " + tasks.size());
    }
}
