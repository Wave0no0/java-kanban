package com.yandex.taskmanager.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.yandex.taskmanager.Status;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {

    @Test
    public void tasksWithEqualIdShouldBeEqual() {
        Task task1 = new Task(1, "Купить хлеб", "В Дикси у дома", Status.NEW);
        Task task2 = new Task(1, "Купить молоко", "В Пятерочке", Status.DONE);
        assertEquals(task1, task2, "Задачи с одинаковыми ID должны быть равны.");
    }

    @Test
    public void taskShouldHaveCorrectDurationAndTime() {
        Duration duration = Duration.ofHours(2);
        LocalDateTime startTime = LocalDateTime.now();
        Task task = new Task(1, "Тестовая задача", "Описание задачи", Status.NEW);
        task.setDuration(duration);
        task.setStartTime(startTime);

        assertEquals(duration, task.getDuration(), "Продолжительность задачи некорректна.");
        assertEquals(startTime, task.getStartTime(), "Дата начала задачи некорректна.");
        assertEquals(startTime.plus(duration), task.getEndTime(), "Дата окончания задачи некорректна.");
    }

    @Test
    public void taskToStringShouldReturnCorrectFormat() {
        Task task = new Task(1, "Купить хлеб", "В Дикси у дома", Status.NEW);
        task.setDuration(Duration.ofHours(1));
        task.setStartTime(LocalDateTime.now());
        String expected = "Task.Task{name='Купить хлеб', description='В Дикси у дома', id=1, status=NEW, duration=PT1H, startTime=" + task.getStartTime() + ", endTime=" + task.getEndTime() + "}";
        assertEquals(expected, task.toString(), "Метод toString() возвращает неправильный формат.");
    }
}