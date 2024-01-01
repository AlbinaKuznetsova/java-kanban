package managers;

import tasks.*;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;

public class FileBackedTasksManager extends InMemoryTaskManager {
    protected File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    private void save() { // метод сохраняет информацию из менеджера в файл
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8); BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("id,type,name,status,description,duration,startTime,epic\n");
            for (Task task : getTasks()) {
                bufferedWriter.write(CSVFormat.toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                bufferedWriter.write(CSVFormat.toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                bufferedWriter.write(CSVFormat.toString(subtask) + "\n");
            }
            bufferedWriter.write("\n");
            bufferedWriter.write(CSVFormat.historyToString(historyManager));
        } catch (IOException exp) {
            throw new ManagerSaveException("Произошла ошибка записи в файл");
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try (FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isBlank()) { // Если дошли до пустой строки, считываем следующую и записываем в историю
                    line = bufferedReader.readLine();
                    for (Integer id : CSVFormat.historyFromString(line)) {
                        manager.getTask(id);
                    }
                } else {
                    Task task = CSVFormat.fromString(line);
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
        switch (task.getType()) {
            case TASK:
                super.createTask(task);
                break;
            case SUBTASK:
                super.createSubtask((Subtask) task);
                break;
            case EPIC:
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

    public static void main(String[] args) {

        FileBackedTasksManager fileManager = new FileBackedTasksManager(new File("saveTasks2.csv"));
        fileManager.createTask(new Task("task1", "Купить автомобиль", 100
                , LocalDateTime.of(2023, Month.DECEMBER,1, 12,0, 0)));
        fileManager.createEpic(new Epic("new Epic1", "Новый Эпик"));
        fileManager.createSubtask(new Subtask("New Subtask", "Подзадача", 2, 30
                , LocalDateTime.of(2023, Month.DECEMBER,19, 12,0, 0)));
        fileManager.createSubtask(new Subtask("New Subtask2", "Подзадача2", 2, 180
                , LocalDateTime.of(2023, Month.DECEMBER,20, 10,0, 0)));
        fileManager.getTaskById(1);
        fileManager.getEpicById(2);
        fileManager.getSubtaskById(3);
        System.out.println(fileManager.getTasks());
        System.out.println(fileManager.getEpics());
        System.out.println(fileManager.getSubtasks());
        System.out.println(fileManager.getHistory());
        System.out.println("\n\n" + "new" + "\n\n");
        FileBackedTasksManager fileBackedTasksManager = loadFromFile(new File("saveTasks2.csv"));
        System.out.println(fileBackedTasksManager.getTasks());
        System.out.println(fileBackedTasksManager.getEpics());
        System.out.println(fileBackedTasksManager.getSubtasks());
        System.out.println(fileBackedTasksManager.getHistory());
    }
}
