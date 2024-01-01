package tests;

import managers.FileBackedTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{
    @BeforeEach
    public void beforeEach() {
        taskManager = new FileBackedTasksManager(new File("tests.csv"));
    }

    @AfterEach //очищаем файл после каждого теста
    public void afterEach() {
        taskManager.deleteAllSubtasks();
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
    }

    @Test
    void loadFromWrongFile() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("12345.csv"));
        assertEquals(0,fileBackedTasksManager.getTasks().size());
        assertEquals(0,fileBackedTasksManager.getSubtasks().size());
        assertEquals(0,fileBackedTasksManager.getEpics().size());

    }
    @Test
    void saveAndLoadFromFile() {
        //Проверяем загрузку из файла с пустым списком задач
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("tests.csv"));
        assertEquals(0, fileBackedTasksManager.getTasks().size(), "Неверное количество задач");
        assertEquals(0, fileBackedTasksManager.getEpics().size(), "Неверное количество задач");
        assertEquals(0, fileBackedTasksManager.getSubtasks().size(), "Неверное количество задач");
        assertEquals(0, fileBackedTasksManager.getHistory().size(), "Неверная история");

        Epic epic = new Epic("new Epic1", "Новый Эпик");
        Epic epic2 = new Epic("new Epic2", "Эпик без подзадач");
        Subtask subtask1 = new Subtask("Task1", "Task1 description",epic.getId(),60
                , LocalDateTime.of(2023, Month.DECEMBER,20, 12,0, 0));
        Subtask subtask2 = new Subtask("Task2", "Task2 description",epic.getId(),30
                , LocalDateTime.of(2023,Month.DECEMBER,22, 14, 0 ,0));
        taskManager.createEpic(epic);
        taskManager.createEpic(epic2); //Эпик без подзадач
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        // Проверяем загрузку из файла с пустой историей
        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("tests.csv"));
        assertEquals(0, fileBackedTasksManager.getTasks().size(), "Неверное количество задач");
        assertEquals(2, fileBackedTasksManager.getEpics().size(), "Неверное количество задач");
        assertEquals(2, fileBackedTasksManager.getSubtasks().size(), "Неверное количество задач");
        assertEquals(0, fileBackedTasksManager.getHistory().size(), "Неверная история");


        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask2.getId());

        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(new File("tests.csv"));
        assertEquals(0, fileBackedTasksManager.getTasks().size(), "Неверное количество задач");
        assertEquals(2, fileBackedTasksManager.getEpics().size(), "Неверное количество задач");
        assertEquals(2, fileBackedTasksManager.getSubtasks().size(), "Неверное количество задач");
        assertEquals(2, fileBackedTasksManager.getHistory().size(), "Неверная история");

        assertTrue(fileBackedTasksManager.getEpics().contains(epic));
        assertTrue(fileBackedTasksManager.getEpics().contains(epic2));
        assertTrue(fileBackedTasksManager.getSubtasks().contains(subtask1));
        assertTrue(fileBackedTasksManager.getSubtasks().contains(subtask2));
        assertTrue(fileBackedTasksManager.getHistory().contains(epic));
        assertTrue(fileBackedTasksManager.getHistory().contains(subtask2));

    }

}

