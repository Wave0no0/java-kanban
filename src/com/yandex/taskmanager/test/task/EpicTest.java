package com.yandex.taskmanager.test.task;

import org.junit.jupiter.api.Test;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.task.Epic;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void epicsWithEqualIdShouldBeEqual() {
        Epic epic1 = new Epic(10, "Сделать ремонт", "Уложиться в 2 миллиона", Status.NEW);
        sprint_6-solution
        Epic epic2 = new Epic(10, "Подготовиться к собеседованию", "1 июля в 11:00", Status.IN_PROGRESS);
        assertEquals(epic1, epic2, "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }

    @Test
    public void epicShouldContainSubtasks() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic.getId());

        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        assertEquals(2, epic.getSubtaskList().size(), "Эпик должен содержать 2 подзадачи.");
        assertTrue(epic.getSubtaskList().contains(subtask1), "Эпик должен содержать подзадачу 1.");
        assertTrue(epic.getSubtaskList().contains(subtask2), "Эпик должен содержать подзадачу 2.");
    }

    @Test
    public void epicToStringShouldReturnCorrectFormat() {
        Epic epic = new Epic(1, "Эпик", "Описание эпика", Status.NEW);
        String expected = "Task.Epic{name= Эпик', description = 'Описание эпика', id=1, subtaskList.size = 0, status = NEW}";
        assertEquals(expected, epic.toString(), "Метод toString() возвращает неправильный формат.");
        Epic epic2 = new Epic(10, "Подготовиться к собеседованию", "1 июля в 11:00",
                Status.IN_PROGRESS);
        assertEquals(epic1, epic2,
                "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
 main
    }
}