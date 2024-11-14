package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    private final HashMap<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        // Если задача уже есть в истории, удаляем её
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        // Создаем новый узел для задачи
        Node newNode = new Node(task);
        linkLast(newNode);
        // Обновляем historyMap с новым узлом
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = historyMap.remove(id);
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
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next; // Если удаляемый узел - голова
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev; // Если удаляемый узел - хвост
        }
    }
}