package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit();

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        assertEquals(0, manager.getTasks().size());

        file.delete();
    }

    @Test
    void testSaveAndLoadMultipleTasks() throws IOException {
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit();

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.addTask(task1);
        manager.addTask(task2);

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);

        assertEquals(2, loadedManager.getTasks().size());
        assertEquals(task1.getName(), loadedManager.getTasks().get(0).getName());
        assertEquals(task2.getName(), loadedManager.getTasks().get(1).getName());

        file.delete();
    }

    @Test
    void testSaveAndLoadEpicWithSubtasks() throws IOException {
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit();

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Epic epic = new Epic("Epic Task", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 Description", epic.getId());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);

        assertEquals(1, loadedManager.getEpics().size());
        assertEquals(2, loadedManager.getSubtasks().size());
        assertEquals(epic.getName(), loadedManager.getEpics().get(0).getName());
        assertEquals(subtask1.getName(), loadedManager.getSubtasks().get(0).getName());
        assertEquals(subtask2.getName(), loadedManager.getSubtasks().get(1).getName());

        // Проверяем, что подзадачи связаны с эпиком
        assertEquals(epic.getId(), subtask1.getEpicID());
        assertEquals(epic.getId(), subtask2.getEpicID());

        file.delete();
    }
    @Test
    void testLoadValidDataFormat() throws IOException {
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit();

        String validData = "1,TASK,Task 1,Description 1\n" +
                "2,TASK,Task 2,Description 2\n";
        Files.writeString(file.toPath(), validData);

        assertDoesNotThrow(() -> new FileBackedTaskManager(file));
    }
    @Test
    void testLoadEpicWithSubtasksAndCheckRelationships() throws IOException {
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit();

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Epic epic = new Epic("Epic Task", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Subtask 1 Description", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask 2 Description", epic.getId());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Сохраняем и загружаем менеджер
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);

        // Проверяем, что подзадачи связаны с эпиком
        assertEquals(epic.getId(), loadedManager.getSubtasks().get(0).getEpicID());
        assertEquals(epic.getId(), loadedManager.getSubtasks().get(1).getEpicID());

        file.delete();
    }
}