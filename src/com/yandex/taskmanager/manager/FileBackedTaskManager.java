package com.yandex.taskmanager.manager;

import com.yandex.taskmanager.task.Epic;
import com.yandex.taskmanager.task.Subtask;
import com.yandex.taskmanager.task.Task;
import com.yandex.taskmanager.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        createFileIfNotExists(); // Создаем файл, если он не существует
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
        // Проверяем, существует ли файл
        if (!file.exists()) {
            return; // Если файл не существует, выходим из метода
        }

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split(System.lineSeparator());

            // Пропускаем заголовок
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    continue; // Игнорируем пустые строки
                }

                String[] parts = line.split(",");
                if (parts.length < 5) {
                    throw new ManagerSaveException("Неверное количество полей в строке: " + line);
                }

                int id = Integer.parseInt(parts[0]);
                String taskType = parts[1];

                switch (taskType) {
                    case "TASK":
                        Task task = new Task(id, parts[2], parts[3], Status.valueOf(parts[4].toUpperCase()));
                        addTask(task);
                        break;
                    case "EPIC":
                        Epic epic = new Epic(id, parts[2], parts[3], Status.valueOf(parts[4].toUpperCase()));
                        addEpic(epic);
                        break;
                    case "SUBTASK":
                        if (parts.length != 6) {
                            throw new ManagerSaveException("Неверное количество полей для подзадачи: " + line);
                        }
                        Subtask subtask = new Subtask(id, parts[2], parts[3], Status.valueOf(parts[4].toUpperCase()), Integer.parseInt(parts[5]));
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
        } catch (Exception e) {
            throw new ManagerSaveException("Неизвестная ошибка при загрузке задач: " + e.getMessage(), e);
        }
    }

    private void saveToFile() {
        try {
            StringBuilder content = new StringBuilder();

            // Записываем заголовки
            content.append("id,type,name,description,status");
            content.append(System.lineSeparator());

            // Записываем задачи
            for (Task task : getTasks()) {
                content.append(task.getId()).append(",")
                        .append("TASK,")
                        .append(task.getName()).append(",")
                        .append(task.getDescription()).append(",")
                        .append(task.getStatus());
                content.append(System.lineSeparator());
            }

            // Записываем эпики
            for (Epic epic : getEpics()) {
                content.append(epic.getId()).append(",")
                        .append("EPIC,")
                        .append(epic.getName()).append(",")
                        .append(epic.getDescription()).append(",")
                        .append(epic.getStatus());
                content.append(System.lineSeparator());
            }

            // Записываем подзадачи
            for (Subtask subtask : getSubtasks()) {
                content.append(subtask.getId()).append(",")
                        .append("SUBTASK,")
                        .append(subtask.getName()).append(",")
                        .append(subtask.getDescription()).append(",")
                        .append(subtask.getStatus()).append(",")
                        .append(subtask.getEpicID());
                content.append(System.lineSeparator());
            }

            // Записываем содержимое в файл
            Files.writeString(file.toPath(), content.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач в файл: " + file.getPath(), e);
        } catch (Exception e) {
            throw new ManagerSaveException("Неизвестная ошибка при сохранении задач: " + e.getMessage(), e);
        }
    }

    @Override
    public Task addTask(Task task) {
        Task savedTask = super.addTask(task);
        saveToFile();
        return savedTask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic savedEpic = super.addEpic(epic);
        saveToFile();
        return savedEpic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        Subtask savedSubtask = super.addSubtask(subtask);
        saveToFile();
        return savedSubtask;
    }

    @Override
    public Task updateTask(Task task) {
        Task updatedTask = super.updateTask(task);
        saveToFile();
        return updatedTask;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic updatedEpic = super.updateEpic(epic);
        saveToFile();
        return updatedEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask updatedSubtask = super.updateSubtask(subtask);
        saveToFile();
        return updatedSubtask;
    }

    @Override
    public Task deleteTaskByID(int id) {
        Task deletedTask = super.deleteTaskByID(id);
        saveToFile();
        return deletedTask;
    }

    @Override
    public Epic deleteEpicByID(int id) {
        Epic deletedEpic = super.deleteEpicByID(id);
        saveToFile();
        return deletedEpic;
    }

    @Override
    public Subtask deleteSubtaskByID(int id) {
        Subtask deletedSubtask = super.deleteSubtaskByID(id);
        saveToFile();
        return deletedSubtask;
    }
}