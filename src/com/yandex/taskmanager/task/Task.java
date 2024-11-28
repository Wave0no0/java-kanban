package com.yandex.taskmanager.task;

import com.yandex.taskmanager.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id; // Идентификатор задачи
    private String name; // Название задачи
    private String description; // Описание задачи
    private Status status; // Статус задачи (например, NEW, IN_PROGRESS, DONE)
    private Duration duration; // Продолжительность задачи
    private LocalDateTime startTime; // Время начала задачи
    private LocalDateTime endTime; // Время завершения задачи

    // Конструктор с параметрами
    public Task(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = startTime != null ? startTime.plus(duration) : null; // Вычисляем время завершения
    }

    // Конструктор без id (для создания новых задач)
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW; // Статус по умолчанию
        this.duration = null; // Значение по умолчанию
        this.startTime = null; // Значение по умолчанию
        this.endTime = null; // Значение по умолчанию
    }

    // Новый конструктор с id
    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ZERO; // Значение по умолчанию
        this.startTime = null; // Значение по умолчанию
        this.endTime = null; // Значение по умолчанию
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
        // Обновляем время завершения при изменении продолжительности
        if (startTime != null) {
            this.endTime = startTime.plus(duration);
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        // Обновляем время завершения при изменении времени начала
        if (duration != null) {
            this.endTime = startTime.plus(duration);
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Метод для проверки пересечения с другой задачей
    public boolean isOverlapping(Task other) {
        if (this.startTime == null || this.endTime == null || other.startTime == null || other.endTime == null) {
            return false; // Если у одной из задач нет времени начала или завершения, не проверяем пересечение
        }
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id; // Сравнение по id
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id); // Генерация хэш-кода по id
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}