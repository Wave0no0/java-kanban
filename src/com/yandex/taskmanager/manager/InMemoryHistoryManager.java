package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head; // Указатель на первый узел
    private Node tail; // Указатель на последний узел
    private HashMap<Integer, Node> taskMap; // HashMap для быстрого доступа к узлам

    public InMemoryHistoryManager() {
        this.taskMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (taskMap.containsKey(task.getId())) {
            // Если задача уже есть в истории, удаляем её узел
            removeNode(taskMap.get(task.getId()));
        }
        // Создаем новый узел для задачи
        Node newNode = new Node(task);
        linkLast(newNode);
        // Сохраняем узел в HashMap
        taskMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = taskMap.remove(id);
        if (nodeToRemove != null) {
            removeNode(nodeToRemove);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    private void linkLast(Node newNode) {
        if (tail == null) {
            head = newNode; // Если список пуст, новый узел становится головой
        } else {
            tail.next = newNode; // Устанавливаем следующий для текущего хвоста
            newNode.prev = tail; // Устанавливаем предыдущий для нового узла
        }
        tail = newNode; // Новый узел становится хвостом
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next; // Устанавливаем следующий для предыдущего узла
        } else {
            head = node.next; // Если удаляем голову, обновляем голову
        }
        if (node.next != null) {
            node.next.prev = node.prev; // Устанавливаем предыдущий для следующего узла
        } else {
            tail = node.prev; // Если удаляем хвост, обновляем хвост
        }
    }
}