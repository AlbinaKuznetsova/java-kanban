import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Приготовить обед", "Сварить суп, кашу и котлеты", "NEW");
        Task task2 = new Task("Собрать коробки для переезда", "Упаковать все вещи", "NEW");
        Subtask subtask1 = new Subtask("Вымыть пол","Вымыть пол в гостиной, спальне, на кухне","NEW",4);
        ArrayList<Integer> subtasksId = new ArrayList<>();
        subtasksId.add(3);
        Epic epic1 = new Epic("Уборка","Сделать генеральную уборку дома", subtasksId);
        Subtask subtask2 = new Subtask("Купить билеты","Запланировать даты отдыха, купить билеты на самолет и забронировать отель","NEW",7);
        Subtask subtask3 = new Subtask("Собрать чемодан","Составить список и сложить в чемодан все вещи","NEW",7);
        subtasksId = new ArrayList<>();
        subtasksId.add(5);
        subtasksId.add(6);
        Epic epic2 = new Epic("Путешествие","Подготовиться к путешествию", subtasksId);
        // Проверяем создание задач
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createSubtask(subtask1);
        manager.createEpic(epic1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createEpic(epic2);
        // Проверяем вывод
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());
        // Проверяем изменение статуса
        task2 = new Task("Собрать коробки для переезда", "Упаковать все вещи", 2, "DONE");
        subtask1 = new Subtask("Вымыть пол","Вымыть пол в гостиной, спальне, на кухне", 3, "DONE",4);
        subtask3 = new Subtask("Собрать чемодан","Составить список и сложить в чемодан все вещи", 6,"DONE",7);
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
        manager.deleteEpicById(7);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getEpics());

    }
}
