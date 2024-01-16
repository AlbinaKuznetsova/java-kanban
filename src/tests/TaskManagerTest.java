package tests;

import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void createTask() {
        Task task = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createNullTask() {
        Task task = null;
        assertThrows(NullPointerException.class, () -> taskManager.createTask(task));
        final List<Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void getTasks() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        Task task1 = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Task task2 = new Task("Task2", "Task2 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");

        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));

        taskManager.deleteAllTasks();
        tasks = taskManager.getTasks();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void getSubtasks() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size(), "Неверное количество задач.");
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Subtask subtask2 = new Subtask("Task2", "Task2 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));

        taskManager.deleteAllSubtasks();
        subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    void getEpics() {
        List<Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size(), "Неверное количество задач.");
        Epic epic1 = new Epic("new Epic1", "Новый Эпик");
        Epic epic2 = new Epic("new Epic2", "Новый Эпик");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));

        taskManager.deleteAllEpics();
        epics = taskManager.getEpics();
        assertEquals(0, epics.size(), "Неверное количество задач.");
    }

    @Test
    void deleteAllTasks() {
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasks().size());
        Task task1 = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Task task2 = new Task("Task2", "Task2 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteAllSubtasks() {
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Subtask subtask2 = new Subtask("Task2", "Task2 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(2, taskManager.getSubtasks().size());
        taskManager.deleteAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(1, taskManager.getEpics().size());
    }

    @Test
    void deleteAllEpics() {
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getEpics().size(), "Неверное количество задач.");
        Epic epic1 = new Epic("new Epic1", "Новый Эпик");
        Epic epic2 = new Epic("new Epic2", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic1.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Subtask subtask2 = new Subtask("Task2", "Task2 description", epic1.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(2, taskManager.getEpics().size());
        assertEquals(2, taskManager.getSubtasks().size());
        taskManager.deleteAllEpics();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size()); //Проверяем, что при удалении эпика удаляются его сабтаски
    }

    @Test
    void getTaskById() {
        Task task1 = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createTask(task1);
        assertEquals(0, taskManager.getHistory().size(), "История должна быть пустой");
        Task taskSaved = taskManager.getTaskById(task1.getId());
        assertEquals(task1, taskSaved, "Неверный таск");
        assertEquals(1, taskManager.getHistory().size(), "Вызов task1 должен добавиться в историю");

        Task taskNull = taskManager.getTaskById(task1.getId() + 100);
        assertNull(taskNull);
    }

    @Test
    void getSubtaskById() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", null, 0
                , null);
        Subtask subtask2 = new Subtask("Task2", "Task2 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(0, taskManager.getHistory().size(), "История должна быть пустой");
        Task subtaskSaved = taskManager.getSubtaskById(subtask1.getId());
        assertEquals(subtask1, subtaskSaved, "Неверный таск");
        subtaskSaved = taskManager.getSubtaskById(subtask2.getId());
        assertEquals(subtask2, subtaskSaved, "Неверный таск");
        assertEquals(2, taskManager.getHistory().size(), "Вызов subtask1 должен добавиться в историю");

        Task subtaskNull = taskManager.getSubtaskById(subtask1.getId() + 100);
        assertNull(subtaskNull);
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task2", "Task2 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);

        assertEquals(0, taskManager.getHistory().size(), "История должна быть пустой");
        Task epicSaved = taskManager.getEpicById(epic.getId());
        assertEquals(epicSaved, epic, "Неверный таск");
        assertEquals(1, taskManager.getHistory().size(), "Вызов subtask1 должен добавиться в историю");

        Task epicNull = taskManager.getEpicById(epic.getId() + 100);
        assertNull(epicNull);
    }

    @Test
    void createSubtask() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);

        final Task savedTask = taskManager.getSubtaskById(subtask1.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subtask1, savedTask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createSubtask(subtask1);

        final Task savedTask = taskManager.getEpicById(epic.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void updateTask() {
        Task task = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createTask(task);
        final int taskId = task.getId();
        task.setStatus(IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(task, taskManager.getTaskById(taskId));

        Task taskNew = new Task("New task", "", taskId + 100, NEW, 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        assertThrows(NullPointerException.class, () -> taskManager.updateTask(taskNew));
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        final int subtaskId = subtask1.getId();
        subtask1.setStatus(IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        assertEquals(subtask1, taskManager.getSubtaskById(subtaskId));

        Subtask subtaskNew = new Subtask("New subtask", "", epic.getId(), subtaskId + 100, NEW, 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        assertThrows(NullPointerException.class, () -> taskManager.updateSubtask(subtaskNew));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        epic.setStatus(IN_PROGRESS);
        taskManager.updateEpic(epic);
        assertEquals(epic, taskManager.getEpicById(epicId));

        Epic epicNew = new Epic("New epic", "", epicId + 100, NEW, 60
                , LocalDateTime.of(2023, Month.DECEMBER, 15, 12, 0, 0));
        assertThrows(NullPointerException.class, () -> taskManager.updateEpic(epicNew));
    }

    @Test
    void deleteTaskById() {
        taskManager.deleteTaskById(1); // Проверяем удаление из пустого списка задач
        assertEquals(0, taskManager.getTasks().size());
        Task task1 = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Task task2 = new Task("Task2", "Task2 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        taskManager.getTaskById(task1.getId());
        assertEquals(1, taskManager.getHistory().size(), "Неверная история задач");
        taskManager.deleteTaskById(task1.getId());
        assertEquals(1, taskManager.getTasks().size());
        assertEquals(0, taskManager.getHistory().size(), "Неверная история задач"); // Проверяем удаление из истории

        taskManager.deleteTaskById(task1.getId() + 100); // Пытаемся удалить несуществующий таск
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void deleteSubtaskById() {
        taskManager.deleteSubtaskById(1); // Проверяем удаление из пустого списка задач
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Subtask subtask2 = new Subtask("Task2", "Task2 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size());
        taskManager.getSubtaskById(subtask1.getId());
        assertEquals(1, taskManager.getHistory().size(), "Неверная история задач");
        taskManager.deleteSubtaskById(subtask1.getId());
        assertEquals(1, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getHistory().size(), "Неверная история задач"); // Проверяем удаление из истории

        assertThrows(NullPointerException.class, () -> taskManager.deleteSubtaskById(subtask1.getId() + 100)); // Пытаемся удалить несуществующий таск
        assertEquals(1, taskManager.getSubtasks().size());
    }

    @Test
    void deleteEpicById() {
        taskManager.deleteEpicById(1); // Проверяем удаление из пустого списка задач
        assertEquals(0, taskManager.getTasks().size());
        Epic epic1 = new Epic("Task1", "Task1 description");
        Epic epic2 = new Epic("Task2", "Task2 description");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(), "Неверное количество тасков");
        taskManager.getEpicById(epic1.getId());
        assertEquals(1, taskManager.getHistory().size(), "Неверная история задач");
        taskManager.deleteEpicById(epic1.getId());
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество тасков");
        assertEquals(0, taskManager.getHistory().size(), "Неверная история задач"); // Проверяем удаление из истории

        assertThrows(NullPointerException.class, () -> taskManager.deleteEpicById(epic1.getId() + 100)); // Пытаемся удалить несуществующий таск
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество тасков");
    }

    @Test
    void getEpicsSubtasks() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        assertEquals(0, taskManager.getEpicsSubtasks(epic.getId()).size(), "Неверное количество задач.");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Subtask subtask2 = new Subtask("Task2", "Task2 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 21, 12, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        List<Subtask> subtasks = taskManager.getEpicsSubtasks(epic.getId());
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));

        taskManager.deleteEpicById(epic.getId()); // Проверяем удаление сабтасков при удалении эпика
        subtasks = taskManager.getEpicsSubtasks(epic.getId());
        assertEquals(0, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    void getTaskStartAndEndTime() {
        Task task = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        taskManager.createTask(task);
        final int taskId = task.getId();

        assertEquals(60, taskManager.getTaskById(taskId).getDuration());
        assertEquals(LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0)
                , taskManager.getTaskById(taskId).getStartTime());
        assertEquals(LocalDateTime.of(2023, Month.DECEMBER, 20, 13, 0, 0)
                , taskManager.getTaskById(taskId).getEndTime());

        Task task1 = new Task("Task1", "Task1 description", 0
                , null);
        taskManager.createTask(task1);
        final int taskId1 = task1.getId();
        assertNull(taskManager.getTaskById(taskId1).getStartTime());
        assertNull(taskManager.getTaskById(taskId1).getEndTime());
    }

    @Test
    void getSubtaskAndEpicStartAndEndTime() {
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Subtask subtask2 = new Subtask("Task1", "Task1 description", epic.getId(), 10
                , LocalDateTime.of(2023, Month.DECEMBER, 22, 19, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        assertEquals(60, taskManager.getSubtaskById(subtask1.getId()).getDuration());
        assertEquals(LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0)
                , taskManager.getSubtaskById(subtask1.getId()).getStartTime());
        assertEquals(LocalDateTime.of(2023, Month.DECEMBER, 20, 13, 0, 0)
                , taskManager.getSubtaskById(subtask1.getId()).getEndTime());

        assertEquals(LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0)
                , taskManager.getEpicById(epic.getId()).getStartTime());
        assertEquals(70, taskManager.getEpicById(epic.getId()).getDuration());
        assertEquals(LocalDateTime.of(2023, Month.DECEMBER, 22, 19, 10, 0)
                , taskManager.getEpicById(epic.getId()).getEndTime());

        Epic epic2 = new Epic("new Epic2", "Новый Эпик");
        taskManager.createEpic(epic2);
        assertNull(taskManager.getEpicById(epic2.getId()).getStartTime());
        assertEquals(0, taskManager.getEpicById(epic2.getId()).getDuration());
        assertNull(taskManager.getEpicById(epic2.getId()).getEndTime());
    }

    @Test
    void getPrioritizedTasks() {
        Task task1 = new Task("Task1", "Task1 description", 60
                , LocalDateTime.of(2023, Month.JANUARY, 15, 12, 0, 0)); // id=1
        Task task2 = new Task("Task2", "Task2 description"); // id=2
        Epic epic1 = new Epic("Epic1", "Epic1 description"); // id=3
        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description", 3, 60
                , LocalDateTime.of(2023, Month.DECEMBER, 15, 12, 0, 0)); // id=4
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description", 3, 20
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0)); // id=5
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3 description", 3, 30
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 15, 0, 0)); // id=6
        Epic epic2 = new Epic("Epic2", "Epic2 description"); // id=7

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(2, taskManager.getTasks().size());
        assertEquals(2, taskManager.getEpics().size());
        assertEquals(3, taskManager.getSubtasks().size());
        assertEquals(5, taskManager.getPrioritizedTasks().size());
        assertEquals(task1, taskManager.getPrioritizedTasks().first());
        assertEquals(task2, taskManager.getPrioritizedTasks().last());

        Task task3 = new Task("Task3", "Task3 description", 60
                , LocalDateTime.of(2023, Month.DECEMBER, 15, 12, 30, 0)); // id=8
        taskManager.createTask(task3); //Таск не должен создаться, так как пересекается по времени с существующим таском
        assertFalse(taskManager.getTasks().contains(task3));
        assertFalse(taskManager.getPrioritizedTasks().contains(task3));

        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasks().size());
        assertEquals(3, taskManager.getPrioritizedTasks().size());
    }
}