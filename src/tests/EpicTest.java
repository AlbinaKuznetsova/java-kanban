package tests;

import managers.FileBackedTasksManager;
import managers.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    TaskManager fileManager;
    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    int epicId;
    @BeforeEach
    void beforeEach() {
        fileManager = new FileBackedTasksManager(new File("testEpic.csv"));
        Epic epic = new Epic("new Epic1", "Новый Эпик");
        epicId = epic.getId();
        fileManager.createEpic(epic);
        subtask1 = new Subtask("New Subtask", "Подзадача", epicId, 60
                , LocalDateTime.of(2023, Month.DECEMBER,10, 10,0, 0));
        subtask2 = new Subtask("New Subtask2", "Подзадача2", epicId, 60
                , LocalDateTime.of(2023, Month.DECEMBER,20, 12,0, 0));
        fileManager.createSubtask(subtask1);
        fileManager.createSubtask(subtask2);
    }
    @Test
    void shouldReturnEpicStatusNewIfNoSubtasks() {
        fileManager.deleteAllSubtasks();
        Assertions.assertEquals(Status.NEW,fileManager.getEpicById(epicId).getStatus());
    }

    @Test
    void shouldReturnEpicStatusDoneIfAllSubtasksHaveStatusDone() {
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        fileManager.updateSubtask(subtask1);
        fileManager.updateSubtask(subtask2);
        assertEquals(Status.DONE,fileManager.getEpicById(epicId).getStatus());
    }
    @Test
    void shouldReturnEpicStatusNewIfAllSubtasksHaveStatusNew() {
        assertEquals(Status.NEW,fileManager.getEpicById(epicId).getStatus());
    }
    @Test
    void shouldReturnEpicStatusInProgressIfSubtasksHaveStatusDoneAndNew() {
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        fileManager.updateSubtask(subtask1);
        fileManager.updateSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS,fileManager.getEpicById(epicId).getStatus());
    }

    @Test
    void shouldReturnEpicStatusInProgressIfSubtasksHaveStatusInProgress() {
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        fileManager.updateSubtask(subtask1);
        fileManager.updateSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS,fileManager.getEpicById(epicId).getStatus());
    }

    @Test
    void getEpicStartAndEndDate() {
        Task epic = fileManager.getEpicById(epicId);
        assertEquals(120,epic.getDuration());
        assertEquals(LocalDateTime.of(2023, Month.DECEMBER,10, 10,0, 0),epic.getStartTime());
        assertEquals(LocalDateTime.of(2023, Month.DECEMBER,20, 13,0, 0),epic.getEndTime());
    }

}