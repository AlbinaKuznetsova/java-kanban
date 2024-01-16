package tests;

import managers.HttpTaskManager;
import managers.KVServer;
import managers.KVTaskClient;
import managers.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    KVServer kvServer;
    @BeforeEach
    public void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        taskManager = new HttpTaskManager("http://localhost:8078/");
    }

    @AfterEach
    public void afterEach() {
        kvServer.stop();
    }

    @Test
    void loadFromWrongServer() {
        HttpTaskManager newManager = HttpTaskManager.load(new KVTaskClient("http://localhost:80/"));
        assertEquals(0, newManager.getTasks().size());
        assertEquals(0, newManager.getSubtasks().size());
        assertEquals(0, newManager.getEpics().size());

    }

    @Test
    void saveAndLoadFromServer() {
        //Проверяем загрузку из пустого сервера
        HttpTaskManager newManager = HttpTaskManager.load(taskManager.getClient());
        assertEquals(0, newManager.getTasks().size(), "Неверное количество задач");
        assertEquals(0, newManager.getEpics().size(), "Неверное количество задач");
        assertEquals(0, newManager.getSubtasks().size(), "Неверное количество задач");
        assertEquals(0, newManager.getHistory().size(), "Неверная история");

        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Epic epic2 = new Epic("new Epic2", "Эпик без подзадач");
        Subtask subtask1 = new Subtask("Task1", "Task1 description", epic.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 12, 0, 0));
        Subtask subtask2 = new Subtask("Task2", "Task2 description", epic.getId(), 30
                , LocalDateTime.of(2023, Month.DECEMBER, 22, 14, 0, 0));
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2); //Эпик без подзадач
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        // Проверяем загрузку из файла с пустой историей
        newManager = HttpTaskManager.load(taskManager.getClient());
        assertEquals(0, newManager.getTasks().size(), "Неверное количество задач");
        assertEquals(2, newManager.getEpics().size(), "Неверное количество задач");
        assertEquals(2, newManager.getSubtasks().size(), "Неверное количество задач");
        assertEquals(0, newManager.getHistory().size(), "Неверная история");


        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask2.getId());

        newManager = HttpTaskManager.load(taskManager.getClient());
        assertEquals(0, newManager.getTasks().size(), "Неверное количество задач");
        assertEquals(2, newManager.getEpics().size(), "Неверное количество задач");
        assertEquals(2, newManager.getSubtasks().size(), "Неверное количество задач");
        assertEquals(2, newManager.getHistory().size(), "Неверная история");

        assertTrue(newManager.getEpics().contains(epic));
        assertTrue(newManager.getEpics().contains(epic2));
        assertTrue(newManager.getSubtasks().contains(subtask1));
        assertTrue(newManager.getSubtasks().contains(subtask2));
        assertTrue(newManager.getHistory().contains(epic));
        assertTrue(newManager.getHistory().contains(subtask2));

    }

}