package com.yandex.taskmanager.task;

import java.util.List;
import java.util.ArrayList;
import com.yandex.taskmanager.Status;

public class Epic extends Task {

    private List<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    @Override
    public String toString() {
        return "Task.Epic{" +
                "name= '" + getName() + '\'' +  //
                ", description = '" + getDescription() + '\'' +  //
                ", id=" + getId() +
                ", subtaskList.size = " + subtaskList.size() +  //
                ", status = " + getStatus() +  //
                '}';
    }
}