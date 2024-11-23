package com.yandex.taskmanager.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.yandex.taskmanager.Status;

import java.util.List;

class EpicTest {

    @Test
    public void epicsWithEqualIdShouldBeEqual() {
        Epic epic1 = new Epic(10, "Сделать ремонт", "Уложиться в 2 миллиона", Status.NEW);
        Epic epic2 = new Epic(10, "Подготовиться к собеседованию", "1 июля в 11:00", Status.IN_PROGRESS);
        assertEquals(epic1, epic2, "Эпики с одинаковыми ID должны быть равны.");
    }

    @Test
    public void epicShouldContainSubtasks() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        List<Subtask> subtasks = epic.getSubtaskList();
        assertEquals(2, subtasks.size(), "Эпик должен содержать 2 подзадачи.");
        assertTrue(subtasks.contains(subtask1), "Эпик должен содержать подзадачу 1.");
        assertTrue(subtasks.contains(subtask2), "Эпик должен содержать подзадачу 2.");
    }

    @Test
    public void epicShouldUpdateStatusBasedOnSubtasks() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        subtask1.setStatus(Status.DONE);
        epic.updateStatus(); // Теперь доступно

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус эпика должен быть 'IN_PROGRESS'.");

        subtask2.setStatus(Status.DONE);
        epic.updateStatus(); // Теперь доступно

        assertEquals(Status.DONE, epic.getStatus(), "Статус эпика должен быть 'DONE'.");
    }

    @Test
    public void epicToStringShouldReturnCorrectFormat() {
        Epic epic = new Epic(1, "Эпик", "Описание эпика", Status.NEW);
        String expected = "Task.Epic{name= 'Эпик', description = 'Описание эпика', id=1, subtaskList.size = 0, duration=PT0S, startTime=null, endTime=null, status = NEW}";
        assertEquals(expected, epic.toString(), "Метод toString() возвращает неправильный формат.");
    }
}