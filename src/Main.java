import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        Task task1 = new Task("Приготовить обед", "Сварить суп, кашу и котлеты");
        Task task2 = new Task("Собрать коробки для переезда", "Упаковать все вещи");
        Epic epic1 = new Epic("Уборка", "Сделать генеральную уборку дома");
        Subtask subtask1 = new Subtask("Вымыть пол", "Вымыть пол в гостиной, спальне, на кухне", 3);
        Epic epic2 = new Epic("Путешествие", "Подготовиться к путешествию");
        Subtask subtask2 = new Subtask("Купить билеты", "Запланировать даты отдыха, купить билеты на самолет и забронировать отель", 5);
        Subtask subtask3 = new Subtask("Собрать чемодан", "Составить список и сложить в чемодан все вещи", 5);
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
        task2.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask3.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task2);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask3);
        System.out.println("-----------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println("-----------");
        System.out.println("Проверяем отображение истории просмотров");
        Task task = manager.getEpicById(3);
        task = manager.getEpicById(5);
        System.out.println(manager.getHistory());
        task = manager.getSubtaskById(6);
        task = manager.getTaskById(1);
        task = manager.getEpicById(3);
        task = manager.getEpicById(3);
        task = manager.getTaskById(2);
        task = manager.getEpicById(3);
        task = manager.getSubtaskById(4);
        task = manager.getEpicById(5);
        task = manager.getSubtaskById(6);
        manager.deleteTaskById(1); // Удалили задачу, в истории осталась
        manager.deleteEpicById(5);
        System.out.println(manager.getHistory());
        // Проверяем удаление задач
        /*manager.deleteTaskById(1);
        System.out.println(manager.getTasks());
        manager.deleteEpicById(5);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());*/

    }
}
