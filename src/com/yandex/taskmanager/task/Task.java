package com.yandex.taskmanager.task;

import com.yandex.taskmanager.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    private final String name;
    private final String description;
    protected int id; // Изменено на protected
    protected Status status; // Изменено на protected
    protected Duration duration; // Поле для продолжительности задачи
    protected LocalDateTime startTime; // Поле для даты начала задачи
    protected LocalDateTime endTime; // Поле для даты окончания задачи

    // Конструктор с ID
    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = Duration.ZERO; // По умолчанию продолжительность 0
        this.startTime = null; // По умолчанию дата начала не задана
        this.endTime = null; // По умолчанию дата окончания не задана
    }

    // Конструктор без ID
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = Duration.ZERO; // По умолчанию продолжительность 0
        this.startTime = null; // По умолчанию дата начала не задана
        this.endTime = null; // По умолчанию дата окончания не задана
    }

    // Новый конструктор с параметрами продолжительности и времени начала
    public Task(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = (startTime != null && duration != null) ? startTime.plus(duration) : null; // Рассчитываем дату окончания
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        // Рассчитываем дату окончания при установке продолжительности
        if (startTime != null) {
            this.endTime = startTime.plus(duration);
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        // Рассчитываем дату окончания при установке времени начала
        if (duration != null) {
            this.endTime = startTime.plus(duration);
        }
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    // Метод для проверки пересечения с другой задачей
    public boolean isOverlapping(Task other) {
        // Если у одной из задач нет времени начала или времени окончания, считаем, что пересечения нет
        if (this.getStartTime() == null || this.getEndTime() == null || other.getStartTime() == null || other.getEndTime() == null) {
            return false;
        }
        // Проверка пересечения временных промежутков
        return !(this.getStartTime().isAfter(other.getEndTime()) || this.getEndTime().isBefore(other.getStartTime()));
    }

}