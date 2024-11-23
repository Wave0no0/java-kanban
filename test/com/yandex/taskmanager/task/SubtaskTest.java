package com.yandex.taskmanager.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.yandex.taskmanager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class SubtaskTest {

    @Test
    public void subtasksWithEqualIdShouldBeEqual() {
        Duration duration = Duration.ofHours(1);
        LocalDateTime startTime = LocalDateTime.now();

        Subtask subtask1 = new Subtask(10, "Купить хлеб", "В Дикси у дома", Status.NEW, 5, duration, startTime);
        Subtask subtask2 = new Subtask(10, "Купить молоко", "В Пятерочке", Status.DONE, 5, duration, startTime);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковыми ID должны быть равны.");
    }

    @Test
    public void subtaskShouldHaveCorrectEpicId() {
        Duration duration = Duration.ofHours(1);
        LocalDateTime startTime = LocalDateTime.now();

        Epic epic = new Epic("Эпик", "Описание эпика");
        Subtask subtask = new Subtask(1, "Подзадача", "Описание подзадачи", Status.NEW, epic.getId(), duration, startTime);

        assertEquals(epic.getId(), subtask.getEpicID(), "Подзадача должна иметь правильный ID эпика.");
    }

    @Test
    public void subtaskToStringShouldReturnCorrectFormat() {
        Duration duration = Duration.ofHours(1);
        LocalDateTime startTime = LocalDateTime.now();

        // Создаем подзадачу
        Subtask subtask = new Subtask(1, "Купить хлеб", "В Дикси у дома", Status.NEW, 5, duration, startTime);

        // Форматируем время без миллисекунд для сравнения
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedStartTime = startTime.format(formatter);
        String formattedEndTime = startTime.plus(duration).format(formatter);

        // Ожидаемый формат строки
        String expected = "Task.Subtask{name='Купить хлеб', description='В Дикси у дома', id=1, epicID=5, status=NEW, duration=PT1H, startTime=" + formattedStartTime + ", endTime=" + formattedEndTime + "}";

        // Проверяем, что возвращаемая строка соответствует ожидаемому формату
        assertEquals(expected, subtask.toString(), "Метод toString() возвращает неправильный формат.");
    }
}