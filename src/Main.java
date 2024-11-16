import com.yandex.taskmanager.manager.TaskManager;
import com.yandex.taskmanager.manager.Managers;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;

public class Main {

    private static final TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {
        addTasks();
        printAllTasks();
        printViewHistory();
    }

    private static void addTasks() {
        Task washFloor = new Task("Помыть полы", "Во всех комнатах");
        taskManager.addTask(washFloor);

        Task washFloorToUpdate = new Task(washFloor.getId(), "Не забыть помыть полы",
                "Можно только кафель", Status.IN_PROGRESS);
        taskManager.updateTask(washFloorToUpdate);
        taskManager.addTask(new Task("Купить продукты", "Список в заметках"));

        Epic flatRenovation = new Epic("Купить мебель", "Нужно выбрать");
        taskManager.addEpic(flatRenovation);
        Subtask flatRenovationSubtask1 = new Subtask("Купить паркет", "Обязательно светлый",
                flatRenovation.getId());
        Subtask flatRenovationSubtask2 = new Subtask("Установить новую технику", "Старую выкинуть",
                flatRenovation.getId());
        Subtask flatRenovationSubtask3 = new Subtask("Заказать кровать", "удобную",
                flatRenovation.getId());
        taskManager.addSubtask(flatRenovationSubtask1);
        taskManager.addSubtask(flatRenovationSubtask2);
        taskManager.addSubtask(flatRenovationSubtask3);
        flatRenovationSubtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(flatRenovationSubtask2);
    }

    private static void printAllTasks() {
        System.out.println("Задачи:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);

            for (Task task : taskManager.getEpicSubtasks(epic)) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
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
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}