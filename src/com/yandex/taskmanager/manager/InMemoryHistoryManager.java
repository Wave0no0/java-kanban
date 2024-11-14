package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final LinkedList<Task> historyList = new LinkedList<>();
    private final HashMap<Integer, Task> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            // Удаляем предыдущую задачу, если она уже была в истории
            remove(task.getId());
        }
        // Добавляем новую задачу в конец истории
        historyList.add(task);
        // Сохраняем задачу в HashMap
        historyMap.put(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        // Удаляем задачу из HashMap
        Task task = historyMap.remove(id);
        if (task != null) {
            // Удаляем задачу из LinkedList
            historyList.remove(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        // Возвращаем копию списка для защиты внутреннего состояния
        return new LinkedList<>(historyList);
    }
}