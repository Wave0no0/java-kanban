import com.yandex.taskmanager.manager.FileBackedTaskManager;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    private static final FileBackedTaskManager taskManager = new FileBackedTaskManager(new File("tasks.csv"));

    public static void main(String[] args) {
        addTasks();
        printAllTasks();
        printViewHistory();
    }

    private static void addTasks() {
        // Задачи с явным указанием id
        Task washFloor = new Task(1, "Помыть полы", "Во всех комнатах", Status.NEW,
                Duration.ofHours(2), LocalDateTime.of(2023, 11, 24, 9, 0));
        taskManager.addTask(washFloor);

        // Обновление задачи
        Task washFloorToUpdate = new Task(washFloor.getId(), "Не забыть помыть полы",
                "Можно только кафель", Status.IN_PROGRESS, Duration.ofHours(3),
                LocalDateTime.of(2024, 11, 24, 12, 0));  // изменили время начала
        taskManager.updateTask(washFloorToUpdate);

        // Добавляем еще одну задачу с уникальным временем
        taskManager.addTask(new Task(2, "Купить продукты", "Список в заметках", Status.NEW,
                Duration.ofHours(1), LocalDateTime.of(2025, 11, 24, 13, 0)));  // изменили время начала

        // Добавляем эпик с указанием id
        Epic flatRenovation = new Epic(3, "Купить мебель", "Нужно выбрать", Status.NEW);
        taskManager.addEpic(flatRenovation);

        // Подзадачи с правильным порядком аргументов и уникальными временными интервалами
        Subtask flatRenovationSubtask1 = new Subtask(4, "Купить паркет", "Обязательно светлый",
                Status.NEW, flatRenovation.getId(), Duration.ofHours(5),
                LocalDateTime.of(2024, 11, 25, 10, 0));  // изменили время

        Subtask flatRenovationSubtask2 = new Subtask(5, "Установить новую технику", "Старую выкинуть",
                Status.NEW, flatRenovation.getId(), Duration.ofHours(3),
                LocalDateTime.of(2024, 11, 26, 14, 0));  // изменили время

        Subtask flatRenovationSubtask3 = new Subtask(6, "Заказать кровать", "Удобную",
                Status.NEW, flatRenovation.getId(), Duration.ofHours(2),
                LocalDateTime.of(2024, 11, 27, 16, 0));  // изменили время

        taskManager.addSubtask(flatRenovationSubtask1);
        taskManager.addSubtask(flatRenovationSubtask2);
        taskManager.addSubtask(flatRenovationSubtask3);

        // Обновляем статус одной из подзадач
        flatRenovationSubtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(flatRenovationSubtask2);
    }

    private static void printAllTasks() {
        System.out.println("Задачи:");
        taskManager.getTasks().forEach(System.out::println);

        System.out.println("Эпики:");
        taskManager.getEpics().forEach(epic -> {
            System.out.println(epic);
            taskManager.getEpicSubtasks(epic).forEach(subtask -> System.out.println("--> " + subtask));
        });

        System.out.println("Подзадачи:");
        taskManager.getSubtasks().forEach(System.out::println);
    }

    private static void printViewHistory() {
        // Просматриваем несколько задач
        taskManager.getTaskByID(1);
        taskManager.getTaskByID(2);
        taskManager.getEpicByID(3);
        taskManager.getTaskByID(1);
        taskManager.getSubtaskByID(4);
        taskManager.getSubtaskByID(5);
        taskManager.getSubtaskByID(6);
        taskManager.getEpicByID(3);
        taskManager.getSubtaskByID(4);
        taskManager.getTaskByID(2);
        taskManager.getSubtaskByID(6);

        System.out.println();
        System.out.println("История просмотров:");
        taskManager.getHistory().forEach(System.out::println);
    }
}
