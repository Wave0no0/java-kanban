package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;
import com.yandex.taskmanager.Status;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

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

        Duration duration = Duration.ofHours(1);
        LocalDateTime startTime1 = LocalDateTime.now();
        LocalDateTime startTime2 = startTime1.plusHours(2); // Устанавливаем время начала для второй задачи через 2 часа

        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW, duration, startTime1);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW, duration, startTime2);
        manager.addTask(task1);
        manager.addTask(task2);

        // Сохраняем и загружаем менеджер
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

        // Установите duration и startTime для подзадач
        subtask1.setDuration(Duration.ofHours(1)); // пример значения
        subtask1.setStartTime(LocalDateTime.now()); // пример значения
        subtask2.setDuration(Duration.ofHours(2)); // пример значения
        subtask2.setStartTime(LocalDateTime.now()); // пример значения

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Сохраняем и загружаем менеджер
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(file);

        assertEquals(1, loadedManager.getEpics().size());
        assertEquals(2, loadedManager.getSubtasks().size());
        assertEquals(epic.getName(), loadedManager.getEpics().get(0).getName());
        assertEquals(subtask1.getName(), loadedManager.getSubtasks().get(0).getName());
        assertEquals(subtask2.getName(), loadedManager.getSubtasks().get(1).getName());

        // Проверяем, что подзадачи связаны с эпиком
        assertEquals(epic.getId(), subtask1.getEpicID());
        assertEquals(epic.getId(), subtask2.getEpicID());

        // Дополнительные проверки для duration и startTime
        assertEquals(subtask1.getDuration(), loadedManager.getSubtasks().get(0).getDuration());
        assertEquals(subtask1.getStartTime(), loadedManager.getSubtasks().get(0).getStartTime());
        assertEquals(subtask2.getDuration(), loadedManager.getSubtasks().get(1).getDuration());
        assertEquals(subtask2.getStartTime(), loadedManager.getSubtasks().get(1).getStartTime());

        file.delete();
    }

    @Test
    void testLoadValidDataFormat() throws IOException {
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit();

        String validData = "id,type,name,description,status,duration,startTime\n" +
                "1,TASK,Task 1,Description 1,NEW,PT1H," + LocalDateTime.now() + "\n" +
                "2,EPIC,Task 2,Description 2,NEW,PT1H," + LocalDateTime.now() + "\n" +
                "3,SUBTASK,Subtask 1,Description 3,NEW,PT1H," + LocalDateTime.now() + ",2\n";
        Files.writeString(file.toPath(), validData);

        assertDoesNotThrow(() -> new FileBackedTaskManager(file));
    }

    @Test
    void testLoadEpicWithSubtasksAndCheckRelationships() throws IOException {
        File file = Files.createTempFile("tasks", ".csv").toFile();
        file.deleteOnExit();

        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        Duration duration = Duration.ofHours(1);
        LocalDateTime startTime = LocalDateTime.now();

        Epic epic = new Epic("Epic Task", "Epic Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask(1, "Subtask 1", "Subtask 1 Description", Status.NEW, epic.getId(), duration, startTime);
        Subtask subtask2 = new Subtask(2, "Subtask 2", "Subtask 2 Description", Status.NEW, epic.getId(), duration, startTime);

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