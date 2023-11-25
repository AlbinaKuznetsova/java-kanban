import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Task1", "Task1 description"); // id=1
        Task task2 = new Task("Task2", "Task2 description"); // id=2
        Epic epic1 = new Epic("Epic1", "Epic1 description"); // id=3
        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description", 3); // id=4
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description", 3); // id=5
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3 description", 3); // id=6
        Epic epic2 = new Epic("Epic2", "Epic2 description"); // id=7
        // Проверяем создание задач
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);

        // Проверяем вывод
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        // Проверяем изменение статуса
        System.out.println("-----------");
        System.out.println("Проверяем изменение статуса");
        task2.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask3.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task2);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask3);
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println("-----------");
        System.out.println("Проверяем отображение истории просмотров");
        Task task = manager.getEpicById(3);
        task = manager.getEpicById(7);
        task = manager.getSubtaskById(6);
        task = manager.getTaskById(1);
        System.out.println(manager.getHistory());
        task = manager.getEpicById(3);
        task = manager.getEpicById(3);
        task = manager.getTaskById(2);
        task = manager.getEpicById(3);
        task = manager.getSubtaskById(4);
        task = manager.getEpicById(7);
        task = manager.getSubtaskById(6);
        System.out.println(manager.getHistory());
        manager.deleteTaskById(1); // Удалили задачу
        System.out.println(manager.getHistory());
        manager.deleteEpicById(3);
        System.out.println(manager.getHistory());
    }
}
