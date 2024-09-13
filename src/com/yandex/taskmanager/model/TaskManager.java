package com.yandex.taskmanager.model;
import com.yandex.taskmanager.service.Status;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Task> tasks = new HashMap<>();

    public TaskManager() {}

    public Epic createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public Task createTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Task addTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask addSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public Epic updateEpic(Epic epic) {
        Integer epicID = epic.getId();
        if (epicID == null || !epics.containsKey(epicID)) {
            return null;
        }
        Epic oldEpic = epics.get(epicID);
        List<Subtask> oldEpicSubtaskList = oldEpic.getSubtaskList();
        List<Subtask> newEpicSubtaskList = epic.getSubtaskList();

        // Обновляем только измененные подзадачи
        for (Subtask subtask : newEpicSubtaskList) {
            if (!oldEpicSubtaskList.contains(subtask)) {
                subtasks.put(subtask.getId(), subtask);
            }
        }
        for (Subtask subtask : oldEpicSubtaskList) {
            if (!newEpicSubtaskList.contains(subtask)) {
                subtasks.remove(subtask.getId());
            }
        }

        epics.replace(epicID, epic);
        updateEpicStatus(epic);
        return epic;
    }

    public void deleteEpicByID(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Subtask> epicSubtasks = epic.getSubtaskList();
            epics.remove(id);
            for (Subtask subtask : epicSubtasks) {
                subtasks.remove(subtask.getId());
            }
        } else {
            System.out.println("Эпик с таким ID не существует");
        }
    }

    public Subtask updateSubtask(Subtask subtask) {
        Integer subtaskID = subtask.getId();
        if (subtaskID == null || !subtasks.containsKey(subtaskID)) {
            return null;
        }
        subtasks.replace(subtaskID, subtask);
        return subtask;
    }

    public void deleteSubtaskByID(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        } else {
            System.out.println("Подзадача с таким ID не существует");
        }
    }

    public Task updateTask(Task task) {
        Integer taskID = task.getId();
        if (taskID == null || !tasks.containsKey(taskID)) {
            return null;
        }
        tasks.replace(taskID, task);
        return task;
    }

    public void deleteTaskByID(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задача с таким ID не существует");
        }
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> list = epic.getSubtaskList();
        boolean hasInProgress = false;
        boolean allDone = true;

        for (Subtask subtask : list) {
            if (subtask.getStatus() == Status.IN_PROGRESS) {
                hasInProgress = true;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (hasInProgress) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
}