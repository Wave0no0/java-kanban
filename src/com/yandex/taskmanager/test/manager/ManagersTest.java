package com.yandex.taskmanager.test.manager;

import com.yandex.taskmanager.manager.InMemoryHistoryManager;
import com.yandex.taskmanager.manager.InMemoryTaskManager;
import com.yandex.taskmanager.manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultShouldInitializeInMemoryTaskManager() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault());
    }

    @Test
    void getDefaultHistoryShouldInitializeInMemoryHistoryManager() {
        assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory());
    }
}