package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_STORAGE = 10;
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (historyList.size() == MAX_HISTORY_STORAGE) {
            historyList.remove(0); // Используем метод remove(0) для удаления первого элемента
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        // Возвращаем копию списка для защиты внутреннего состояния
        return new ArrayList<>(historyList);
    }
}