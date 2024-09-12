public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task buyGroceries = new Task("Купить продукты", "Не забыть молоко и хлеб");
        Task buyGroceriesCreated = taskManager.addTask(buyGroceries);
        System.out.println(buyGroceriesCreated);

        Task buyGroceriesToUpdate = new Task(buyGroceries.getId(), "Купить продукты", "Можно и без молока",
                Status.IN_PROGRESS);
        Task buyGroceriesUpdated = taskManager.updateTask(buyGroceriesToUpdate);
        System.out.println(buyGroceriesUpdated);

        Epic moveApartments = new Epic("Переезд в новую квартиру", "Нужно успеть до конца месяца");
        taskManager.addEpic(moveApartments);
        System.out.println(moveApartments);
        Subtask moveApartmentsSubtask1 = new Subtask("Упаковать вещи", "Обязательно не забыть документы",
                moveApartments.getId());
        Subtask moveApartmentsSubtask2 = new Subtask("Заказать грузчиков", "Старую мебель продать на Авито",
                moveApartments.getId());
        taskManager.addSubtask(moveApartmentsSubtask1);
        taskManager.addSubtask(moveApartmentsSubtask2);
        System.out.println(moveApartments);
        moveApartmentsSubtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(moveApartmentsSubtask2);
        System.out.println(moveApartments);
    }
}