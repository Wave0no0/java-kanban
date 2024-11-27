package com.yandex.taskmanager.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.yandex.taskmanager.Status;

public class Epic extends Task {

    private List<Subtask> subtaskList = new ArrayList<>();
    private Duration duration; // Общая продолжительность
    private LocalDateTime startTime; // Дата и время начала самого раннего подзадачи
    private LocalDateTime endTime; // Дата и время завершения самой поздней подзадачи

    public Epic(String name, String description) {
        super(name, description);
        this.duration = Duration.ZERO; // По умолчанию 0 минут
        this.startTime = null; // По умолчанию нет времени начала
        this.endTime = null; // По умолчанию нет времени завершения
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status); // Теперь этот вызов будет работать
        this.duration = Duration.ZERO; // По умолчанию 0 минут
        this.startTime = null; // По умолчанию нет времени начала
        this.endTime = null; // По умолчанию нет времени завершения
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        updateEpicDetails(); // Обновляем детали эпика при добавлении подзадачи
    }

    public void clearSubtasks() {
        subtaskList.clear();
        duration = Duration.ZERO; // Сбрасываем продолжительность
        startTime = null; // Сбрасываем время начала
        endTime = null; // Сбрасываем время завершения
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
        updateEpicDetails(); // Обновляем детали эпика при изменении подзадач
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Метод для обновления деталей эпика
    public void updateEpicDetails() {
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;

        for (Subtask subtask : subtaskList) {
            duration = duration.plus(subtask.getDuration());
            if (startTime == null || (subtask.getStartTime() != null && subtask.getStartTime().isBefore(startTime))) {
                startTime = subtask.getStartTime();
            }
            if (endTime == null || (subtask.getEndTime() != null && subtask.getEndTime().isAfter(endTime))) {
                endTime = subtask.getEndTime();
            }
        }
        // Обновляем статус эпика в зависимости от статусов подзадач
        updateStatus();
    }

    protected void updateStatus() {
        int allIsDoneCount = 0;
        int allIsInNewCount = 0;
        List<Subtask> list = getSubtaskList();

        for (Subtask subtask : list) {
            if (subtask.getStatus() == Status.DONE) {
                allIsDoneCount++;
            }
            if (subtask.getStatus() == Status.NEW) {
                allIsInNewCount++;
            }
        }
        if (allIsDoneCount == list.size()) {
            setStatus(Status.DONE);
        } else if (allIsInNewCount == list.size()) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Task.Epic{" +
                "name= '" + getName() + '\'' +
                ", description = '" + getDescription() + '\'' +
                ", id=" + getId() +
                ", subtaskList.size = " + subtaskList.size() +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status = " + getStatus() +
                '}';
    }
}