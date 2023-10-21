import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();
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
        task2.setStatus("DONE");
        subtask1.setStatus("DONE");
        subtask3.setStatus("IN_PROGRESS");
        manager.updateTask(task2);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask3);
        System.out.println("-----------");
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        System.out.println("-----------");
        // Проверяем удаление задач
        manager.deleteTaskById(1);
        System.out.println(manager.getTasks());
        manager.deleteEpicById(5);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());


       /* Manager taskTracker = new Manager();
        Epic epic1 = new Epic("Эпик 1","Нужно сделать");
        taskTracker.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask2 создания ",
                "Написать что то ", epic1.getId());
        taskTracker.createSubtask(subtask2);
        System.out.println(epic1);
        subtask1.setStatus("IN_PROGRESS");
        taskTracker.updateSubtask(subtask1);
        System.out.println(epic1);
        subtask2.setStatus("DONE");
        taskTracker.updateSubtask(subtask2);
        System.out.println(epic1);
        subtask1.setStatus("DONE");
        taskTracker.updateSubtask(subtask1);*/

    }
}
