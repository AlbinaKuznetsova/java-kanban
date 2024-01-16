
import managers.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.Month;

public class Main {

    public static void main(String[] args) {
        try {
            new KVServer().start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        HttpTaskManager manager = (HttpTaskManager) Managers.getDefault();
        manager.createTask(new Task("task1", "Купить автомобиль", 100
                , LocalDateTime.of(2023, Month.DECEMBER,1, 12,0, 0)));
        manager.createEpic(new Epic("new Epic1", "Новый Эпик"));
        manager.createSubtask(new Subtask("New Subtask", "Подзадача", 2, 30
                , LocalDateTime.of(2023, Month.DECEMBER,19, 12,0, 0)));
        manager.createSubtask(new Subtask("New Subtask2", "Подзадача2", 2, 180
                , LocalDateTime.of(2023, Month.DECEMBER,20, 10,0, 0)));
        manager.getTaskById(1);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getHistory());
        System.out.println(manager.getPrioritizedTasks());
        System.out.println("new load HttpTaskManager-----------");
        TaskManager manager2 = HttpTaskManager.load(manager.getClient());
        System.out.println(manager2.getTasks());
        System.out.println(manager2.getEpics());
        System.out.println(manager2.getSubtasks());
        System.out.println(manager2.getHistory());
    }
}
