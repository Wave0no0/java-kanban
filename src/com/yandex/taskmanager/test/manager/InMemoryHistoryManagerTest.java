package com.yandex.taskmanager.test.manager;

import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.manager.Managers;
import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;
import com.yandex.taskmanager.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getHistoryShouldReturnListOf10Tasks() {
        for (int i = 0; i < 20; i++) {
            taskManager.addTask(new Task("Task " + i, "Description " + i));
        }

        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskByID(task.getId());
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "Неверное количество элементов в истории.");
    }

    @Test
    public void getHistoryShouldReturnOldTaskAfterUpdate() {
        Task task = new Task("Помыть полы", "С новым средством");
        taskManager.addTask(task);
        taskManager.getTaskByID(task.getId());
        taskManager.updateTask(new Task(task.getId(), "Не забыть помыть полы", "Можно и без средства", Status.IN_PROGRESS));

        List<Task> history = taskManager.getHistory();
        Task oldTask = history.get(0);
        assertEquals(task.getName(), oldTask.getName(), "В истории не сохранилась старая версия задачи.");
        assertEquals(task.getDescription(), oldTask.getDescription(), "В истории не сохранилась старая версия задачи.");
    }

    @Test
    public void getHistoryShouldReturnOldEpicAfterUpdate() {
        Epic epic = new Epic("Сделать ремонт", "Нужно успеть за отпуск");
        taskManager.addEpic(epic);
        taskManager.getEpicByID(epic.getId());
        taskManager.updateEpic(new Epic(epic.getId(), "Новое имя", "Новое описание", Status.IN_PROGRESS));

        List<Task> history = taskManager.getHistory();
        Epic oldEpic = (Epic) history.get(0);
        assertEquals(epic.getName(), oldEpic.getName(), "В истории не сохранилась старая версия эпика.");
        assertEquals(epic.getDescription(), oldEpic.getDescription(), "В истории не сохранилась старая версия эпика.");
    }

    @Test
    public void getHistoryShouldReturnOldSubtaskAfterUpdate() {
        Epic epic = new Epic("Сделать ремонт", "Нужно успеть за отпуск");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Заказать книжный шкаф", "Из темного дерева", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.getSubtaskByID(subtask.getId());
        taskManager.updateSubtask(new Subtask(subtask.getId(), "Новое имя", "Новое описание", Status.IN_PROGRESS, epic.getId()));

        List<Task> history = taskManager.getHistory();
        Subtask oldSubtask = (Subtask) history.get(0);
        assertEquals(subtask.getName(), oldSubtask.getName(), "В истории не сохранилась старая версия подзадачи.");
        assertEquals(subtask.getDescription(), oldSubtask.getDescription(), "В истории не сохранилась старая версия подзадачи.");
    }
}