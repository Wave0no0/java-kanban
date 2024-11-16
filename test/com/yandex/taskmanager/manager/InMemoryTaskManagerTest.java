package com.yandex.taskmanager.manager;

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

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    void addNewTask() {
        final Task task = taskManager.addTask(new Task("Test addNewTask", "Test addNewTask description"));
        final Task savedTask = taskManager.getTaskByID(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpicAndSubtasks() {
        final Epic epic = taskManager.addEpic(new Epic("Сделать ремонт", "Нужно успеть за отпуск"));
        final Subtask subtask1 = taskManager.addSubtask(new Subtask("Поклеить обои", "Обязательно светлые!", epic.getId()));
        final Subtask subtask2 = taskManager.addSubtask(new Subtask("Установить новую технику", "Старую продать на Авито", epic.getId()));
        final Subtask subtask3 = taskManager.addSubtask(new Subtask("Заказать книжный шкаф", "Из темного дерева", epic.getId()));

        final Epic savedEpic = taskManager.getEpicByID(epic.getId());
        final Subtask savedSubtask1 = taskManager.getSubtaskByID(subtask1.getId());
        final Subtask savedSubtask2 = taskManager.getSubtaskByID(subtask2.getId());
        final Subtask savedSubtask3 = taskManager.getSubtaskByID(subtask3.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedSubtask1, "Подзадача 1 не найдена.");
        assertNotNull(savedSubtask2, "Подзадача 2 не найдена.");
        assertNotNull(savedSubtask3, "Подзадача 3 не найдена.");

        assertEquals(epic, savedEpic, "Эпики не совпадают.");
        assertEquals(subtask1, savedSubtask1, "Подзадача 1 не совпадает.");
        assertEquals(subtask2, savedSubtask2, "Подзадача 2 не совпадает.");
        assertEquals(subtask3, savedSubtask3, "Подзадача 3 не совпадает.");

        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    public void updateTaskShouldReturnTaskWithTheSameId() {
        final Task expected = new Task("имя", "описание");
        taskManager.addTask(expected);
        final Task updatedTask = new Task(expected.getId(), "новое имя", "новое описание", Status.DONE);
        final Task actual = taskManager.updateTask(updatedTask);
        assertEquals(expected, actual, "Вернулась задача с другим id.");
    }

    @Test
    public void deleteTaskByIdShouldReturnNullIfKeyIsMissing() {
        taskManager.addTask(new Task(1, "Купить книги", "Список в заметках", Status.NEW));
        taskManager.addTask(new Task(2, "Помыть полы", "С новым средством", Status.DONE));
        assertNull(taskManager.deleteTaskByID(3), "Удаление несуществующей задачи должно вернуть null.");
    }

    @Test
    public void deleteEpicByIdShouldReturnNullIfKeyIsMissing() {
        taskManager.addEpic(new Epic(1, "Сделать ремонт", "Нужно успеть за отпуск", Status.IN_PROGRESS));
        taskManager.deleteEpicByID(1);
        assertNull(taskManager.deleteEpicByID(1), "Удаление несуществующего эпика должно вернуть null.");
    }

    @Test
    public void deleteSubtaskByIdShouldReturnNullIfKeyIsMissing() {
        Epic epic = new Epic("Сделать ремонт", "Нужно успеть за отпуск");
        taskManager.addEpic(epic);
        taskManager.addSubtask(new Subtask("Поклеить обои", "Обязательно светлые!", epic.getId()));
        taskManager.addSubtask(new Subtask("Установить новую технику", "Старую продать на Авито", epic.getId()));
        taskManager.addSubtask(new Subtask("Заказать книжный шкаф", "Из темного дерева", epic.getId()));

        assertNull(taskManager.deleteSubtaskByID(5), "Удаление несуществующей подзадачи должно вернуть null.");
    }

    @Test
    void taskCreatedAndTaskAddedShouldHaveSameVariables() {
        Task expected = new Task(1, "Помыть полы", "С новым средством", Status.DONE);
        taskManager.addTask(expected);
        List<Task> list = taskManager.getTasks();
        Task actual = list.get(0);
        assertEquals(expected.getId(), actual.getId(), "ID задач не совпадают.");
        assertEquals(expected.getName(), actual.getName(), "Имена задач не совпадают.");
        assertEquals(expected.getDescription(), actual.getDescription(), "Описание задач не совпадает.");
        assertEquals(expected.getStatus(), actual.getStatus(), "Статусы задач не совпадают.");
    }
}