package com.yandex.taskmanager.test;

import org.junit.jupiter.api.Test;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.task.Subtask;
import com.yandex.taskmanager.task.Epic;


import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    public void subtasksWithEqualIdShouldBeEqual() {
        Subtask subtask1 = new Subtask(10, "Купить хлеб", "В Дикси у дома", Status.NEW, 5);
        Subtask subtask2 = new Subtask(10, "Купить молоко", "В Пятерочке", Status.DONE, 5);
        assertEquals(subtask1, subtask2, "Ошибка! Наследники класса Task должны быть равны друг другу, если равен их id;");
    }

    @Test
    public void subtaskShouldHaveCorrectEpicId() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        assertEquals(epic.getId(), subtask.getEpicID(), "Подзадача должна иметь правильный ID эпика.");
    }

    @Test
    public void subtaskToStringShouldReturnCorrectFormat() {
        Subtask subtask = new Subtask(1, "Купить хлеб", "В Дикси у дома", Status.NEW, 5);
        String expected = "Task.Subtask{name='Купить хлеб', description='В Дикси у дома', id=1, epicID=5, status=NEW}";
        assertEquals(expected, subtask.toString(), "Метод toString() возвращает неправильный формат.");
    }
}