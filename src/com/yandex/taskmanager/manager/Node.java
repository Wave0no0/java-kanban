package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Task;

public class Node {
    Task task; // Задача, которую хранит узел
    Node prev; // Ссылка на предыдущий узел
    Node next; // Ссылка на следующий узел

    public Node(Task task) {
        this.task = task;
    }
}