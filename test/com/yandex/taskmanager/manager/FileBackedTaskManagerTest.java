package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        // Создаем временный файл
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit(); // Удаляем файл при завершении программы

        // Создаем менеджер и загружаем из пустого файла
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Проверяем, что задач нет
        assertEquals(0, manager.getTasks().size());

        // Удаляем файл после теста
        file.delete();
    }

    @Test
    void testSaveAndLoadMultipleTasks() throws IOException {
        // Создаем временный файл
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit(); // Удаляем файл при завершении программы

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Добавляем несколько задач
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.addTask(task1);
        manager.addTask(task2);

        // Создаем новый менеджер для загрузки из файла
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);

        // Проверяем, что задачи загружены корректно
        assertEquals(2, loadedManager.getTasks().size());
        assertEquals(task1.getName(), loadedManager.getTasks().get(0).getName());
        assertEquals(task2.getName(), loadedManager.getTasks().get(1).getName());

        // Удаляем файл после теста
        file.delete();
    }

    @Test
    void testSaveAndLoadEpicWithSubtasks() throws IOException {
        // Создаем временный файл
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit(); // Удаляем файл при завершении программы

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Создаем эпик
        Epic epic = new Epic("Epic Task", "Epic Description");
        manager.addEpic(epic); // Добавляем эпик в менеджер

        // Создаем подзадачи с правильным идентификатором эпика
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 Description", epic.getId());

        manager.addSubtask(subtask1); // Добавляем подзадачи в менеджер
        manager.addSubtask(subtask2);

        // Создаем новый менеджер для загрузки из файла
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);

        // Проверяем, что эпик и подзадачи загружены корректно
        assertEquals(1, loadedManager.getEpics().size());
        assertEquals(2, loadedManager.getSubtasks().size());
        assertEquals(epic.getName(), loadedManager.getEpics().get(0).getName());
        assertEquals(subtask1.getName(), loadedManager.getSubtasks().get(0).getName());
        assertEquals(subtask2.getName(), loadedManager.getSubtasks().get(1).getName());

        // Удаляем файл после теста
        file.delete();
    }
}