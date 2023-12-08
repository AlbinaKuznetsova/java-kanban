package managers;

import tasks.*;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    protected File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    private void save() { // метод сохраняет информацию из менеджера в файл
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getTasks()) {
                bufferedWriter.write(toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                bufferedWriter.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                bufferedWriter.write(toString(subtask) + "\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(historyToString(historyManager));
        } catch (IOException exp) {
            throw new ManagerSaveException("Произошла ошибка записи в файл");
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    private String toString(Task task) {
        TypeOfTask type = null;
        String epicId = "";
        if (task.getClass().getName().equals("tasks.Task")) {
            type = TypeOfTask.TASK;
        } else if (task.getClass().getName().equals("tasks.Subtask")) {
            type = TypeOfTask.SUBTASK;
            epicId = ((Subtask) task).getEpicId().toString();
        } else if (task.getClass().getName().equals("tasks.Epic")) {
            type = TypeOfTask.EPIC;
        }
        return task.getId() + "," + type + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + epicId;
    }

    private static String historyToString(HistoryManager manager) {
        String historyIds = "";
        List<Task> history = manager.getHistory();

        if (!history.isEmpty()) {
            for (Task task : history) {
                historyIds += task.getId() + ",";
            }
        }
        return historyIds;
    }

    private static Task fromString(String value) {
        String[] values = value.split(",");
        Task task = null;
        if (values[1].equals(TypeOfTask.TASK.toString())) {
            task = new Task(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]));
        } else if (values[1].equals(TypeOfTask.SUBTASK.toString())) {
            task = new Subtask(values[2], values[4], Integer.parseInt(values[5]), Integer.parseInt(values[0]), Status.valueOf(values[3]));
        } else if (values[1].equals(TypeOfTask.EPIC.toString())) {
            task = new Epic(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]));
        }
        return task;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] historyIds = value.split(",");
        for (String historyId : historyIds) {
            if (!historyId.isBlank()) {
                history.add(Integer.parseInt(historyId));
            }
        }
        Collections.reverse(history);
        return history;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isBlank()) { // Если дошли до пустой строки, считываем следующую и записываем в историю
                    line = bufferedReader.readLine();
                    for (Integer id : historyFromString(line)) {
                        manager.getTask(id);
                    }
                } else {
                    Task task = fromString(line);
                    manager.addTask(task);
                }
            }
        } catch (IOException exp) {
            System.out.println(exp.getMessage());
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
        return manager;
    }

    private void addTask(Task task) {
        switch (task.getClass().getName()) {
            case "tasks.Task":
                super.createTask(task);
                break;
            case "tasks.Subtask":
                super.createSubtask((Subtask) task);
                break;
            case "tasks.Epic":
                super.createEpic((Epic) task);
                break;
        }
    }

    private void getTask(Integer id) { // получаем таск по id, чтобы восстановить историю
        Task task = null;
        try {
            task = super.getTaskById(id);
        } catch (NullPointerException exp) {

        }
        if (task == null) {
            try {
                super.getSubtaskById(id);
            } catch (NullPointerException exp) {

            }
        }
        if (task == null) {
            try {
                super.getEpicById(id);
            } catch (NullPointerException exp) {

            }
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Task getSubtaskById(Integer id) {
        Task task = super.getSubtaskById(id);
        save();
        return task;
    }

    @Override
    public Task getEpicById(Integer id) {
        Task task = super.getEpicById(id);
        save();
        return task;
    }
}
