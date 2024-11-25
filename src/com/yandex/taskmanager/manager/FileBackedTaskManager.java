package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;
import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.TreeSet;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private TreeSet<Task> prioritizedTasks;  // Кэшируем приоритетные задачи
    private boolean isPrioritizedTasksValid = false;  // Флаг актуальности приоритетных задач

    public FileBackedTaskManager(File file) {
        this.file = file;
        createFileIfNotExists();  // Создаем файл, если он не существует
        loadFromFile();
    }

    private void createFileIfNotExists() {
        try {
            if (!file.exists()) {
                file.createNewFile(); // Создаем новый файл
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл: " + file.getPath(), e);
        }
    }

    private void loadFromFile() {
        if (!file.exists()) {
            return; // Если файл не существует, выходим из метода
        }

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split(System.lineSeparator());

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    continue; // Игнорируем пустые строки
                }

                String[] parts = line.split(",");
                // Проверяем количество полей
                if (parts.length < 5) {
                    throw new ManagerSaveException("Неверное количество полей в строке: " + line);
                }

                int id = Integer.parseInt(parts[0]);
                String taskType = parts[1];

                switch (taskType) {
                    case "TASK":
                        if (parts.length != 7) { // Задачи имеют 7 полей
                            throw new ManagerSaveException("Неверное количество полей для задачи: " + line);
                        }
                        Task task = createTask(parts, id);
                        addTask(task);
                        break;
                    case "EPIC":
                        if (parts.length != 7) { // Эпики имеют 7 полей
                            throw new ManagerSaveException("Неверное количество полей для эпика: " + line);
                        }
                        Epic epic = createEpic(parts, id);
                        addEpic(epic);
                        break;
                    case "SUBTASK":
                        if (parts.length != 8) { // Подзадачи имеют 8 полей
                            throw new ManagerSaveException("Неверное количество полей для подзадачи: " + line);
                        }
                        Subtask subtask = createSubtask(parts, id);
                        addSubtask(subtask);
                        break;
                    default:
                        throw new ManagerSaveException("Неизвестный тип задачи: " + taskType);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось загрузить задачи из файла: " + file.getPath(), e);
        } catch (NumberFormatException e) {
            throw new ManagerSaveException("Ошибка формата данных в файле: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ManagerSaveException("Неверный статус задачи: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ManagerSaveException("Неизвестная ошибка при загрузке задач: " + e.getMessage(), e);
        }
    }

    private Task createTask(String[] parts, int id) {
        Duration duration = parts[5].isEmpty() || parts[5].equals("null") ? null : Duration.parse(parts[5]);
        LocalDateTime startTime = parts[6].isEmpty() || parts[6].equals("null") ? null : LocalDateTime.parse(parts[6], DATE_TIME_FORMATTER);

        return new Task(id, parts[2], parts[3], Status.valueOf(parts[4].toUpperCase()), duration, startTime);
    }

    private Epic createEpic(String[] parts, int id) {
        return new Epic(id, parts[2], parts[3], Status.valueOf(parts[4].toUpperCase()));
    }

    private Subtask createSubtask(String[] parts, int id) {
        Duration duration = parts[5].isEmpty() || parts[5].equals("null") ? null : Duration.parse(parts[5]);
        LocalDateTime startTime = parts[6].isEmpty() || parts[6].equals("null") ? null : LocalDateTime.parse(parts[6], DATE_TIME_FORMATTER);
        int epicID = Integer.parseInt(parts[7]);

        return new Subtask(id, parts[2], parts[3], Status.valueOf(parts[4].toUpperCase()), epicID, duration, startTime);
    }

    private void saveToFile() {
        StringBuilder content = new StringBuilder();
        try {
            // Записываем заголовки
            content.append("id,type,name,description,status,duration,startTime,epicID").append(System.lineSeparator());

            // Записываем задачи
            for (Task task : getTasks()) {
                content.append(task.getId()).append(",")
                        .append("TASK,")
                        .append(task.getName()).append(",")
                        .append(task.getDescription()).append(",")
                        .append(task.getStatus()).append(",")
                        .append(task.getDuration() != null ? task.getDuration() : "null").append(",")
                        .append(task.getStartTime() != null ? task.getStartTime().format(DATE_TIME_FORMATTER) : "null")
                        .append(System.lineSeparator());
            }

            // Записываем эпики
            for (Epic epic : getEpics()) {
                content.append(epic.getId()).append(",")
                        .append("EPIC,")
                        .append(epic.getName()).append(",")
                        .append(epic.getDescription()).append(",")
                        .append(epic.getStatus()).append(",")
                        .append("null,null") // Эпики не имеют duration и startTime
                        .append(System.lineSeparator());
            }

            // Записываем подзадачи
            for (Subtask subtask : getSubtasks()) {
                content.append(subtask.getId()).append(",")
                        .append("SUBTASK,")
                        .append(subtask.getName()).append(",")
                        .append(subtask.getDescription()).append(",")
                        .append(subtask.getStatus()).append(",")
                        .append(subtask.getDuration() != null ? subtask.getDuration() : "null").append(",")
                        .append(subtask.getStartTime() != null ? subtask.getStartTime().format(DATE_TIME_FORMATTER) : "null").append(",")
                        .append(subtask.getEpicID())
                        .append(System.lineSeparator());
            }

            // Записываем содержимое в файл
            Files.writeString(file.toPath(), content.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач в файл: " + file.getPath(), e);
        }
    }

    private void updatePrioritizedTasks() {
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
        prioritizedTasks.addAll(getTasks());
        prioritizedTasks.addAll(getSubtasks());
        isPrioritizedTasksValid = true;  // Список актуален
    }

    @Override
    public Task addTask(Task task) {
        Task savedTask = super.addTask(task);
        saveToFile();
        isPrioritizedTasksValid = false;  // Список нужно обновить
        return savedTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic savedEpic = super.addEpic(epic);
        saveToFile();
        isPrioritizedTasksValid = false;
        return savedEpic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask savedSubtask = super.addSubtask(subtask);
        saveToFile();
        isPrioritizedTasksValid = false;
        return savedSubtask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        saveToFile();
        isPrioritizedTasksValid = false;
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        saveToFile();
        isPrioritizedTasksValid = false;
        return updatedEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtask(subtask);
        saveToFile();
        isPrioritizedTasksValid = false;
        return updatedSubtask;
    }

    @Override
    public Task deleteTaskByID(int id) {
        Task deletedTask = super.deleteTaskByID(id);
        saveToFile();
        isPrioritizedTasksValid = false;
        return deletedTask;
    }

    @Override
    public Epic deleteEpicByID(int id) {
        Epic deletedEpic = super.deleteEpicByID(id);
        saveToFile();
        isPrioritizedTasksValid = false;
        return deletedEpic;
    }

    @Override
    public Subtask deleteSubtaskByID(int id) {
        Subtask deletedSubtask = super.deleteSubtaskByID(id);
        saveToFile();
        isPrioritizedTasksValid = false;
        return deletedSubtask;
    }

    public TreeSet<Task> getPrioritizedTasks() {
        if (!isPrioritizedTasksValid) {
            updatePrioritizedTasks();  // Обновляем список, если он устарел
        }
        return prioritizedTasks;
    }
}
