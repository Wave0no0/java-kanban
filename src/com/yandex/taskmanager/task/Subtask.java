package com.yandex.taskmanager.task;

import com.yandex.taskmanager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {

    private final int epicID;
    private LocalDateTime startTime; // Время начала
    private Duration duration; // Продолжительность

    // Конструктор с параметрами
    public Subtask(int id, String name, String description, Status status, int epicID, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status);
        this.epicID = epicID;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = (startTime != null && duration != null) ? startTime.plus(duration) : null;
    }

    // Конструктор без параметров времени и продолжительности
    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
        this.duration = Duration.ZERO;
        this.startTime = null;
    }

    // Геттеры и сеттеры
    public int getEpicID() {
        return epicID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String formattedStartTime = (startTime != null) ? startTime.format(formatter) : "null";
        String formattedEndTime = (getEndTime() != null) ? getEndTime().format(formatter) : "null";

        return "Task.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", epicID=" + epicID +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + formattedStartTime +
                ", endTime=" + formattedEndTime +
                '}';
    }
}