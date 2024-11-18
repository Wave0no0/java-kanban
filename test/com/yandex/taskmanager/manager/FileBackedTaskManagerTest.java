package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.manager.FileBackedTaskManager;
import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.task.*;

import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void testSaveAndLoadTasks() {
        // Создаем файл
        File file = new File("tasks.csv");
        file.delete(); // Удаляем файл, если он существует
        file.deleteOnExit(); // Удаляем файл при завершении программы

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Добавляем задачи
        Task task = new Task("Test Task", "Description");
        manager.addTask(task); // Этот метод уже вызывает saveToFile()

        // Создаем новый менеджер для загрузки из файла
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);

        // Проверяем, что задачи загружены корректно
        assertEquals(1, loadedManager.getTasks().size());
        assertEquals(task.getName(), loadedManager.getTasks().get(0).getName());

        // Удаляем файл после теста
        file.delete();
    }
}