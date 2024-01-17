package tests;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest {
    HistoryManager historyManager;
    @BeforeEach
    void beforeEach(){
        historyManager = new InMemoryHistoryManager();
    }
    @Test
    void addOneTaskToHistory() {
        assertEquals(0,historyManager.getHistory().size(), "История должна быть пустой");
        Task task = new Task("Task1", "Task1 description");
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть пустой");
        assertEquals(1, history.size(), "История не должна быть пустой");
    }
    @Test
    void addManyTaskToHistory() {
        Task task1 = new Task("Task1", "Task1 description");
        Task task2 = new Task("Task2", "Task2 description");
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть пустой");
        assertEquals(2, history.size(), "В истории должно содержаться 2 элемента");
        assertEquals(task2, history.get(0), "Должен вернуться task2");

        historyManager.add(task1);
        history = historyManager.getHistory();
        assertEquals(2, history.size(), "Первый просмотр task1 должен удалиться");
        assertEquals(task1, history.get(0), "Должен вернуться task1");
    }
    @Test
    void removeFromHistory(){
        assertEquals(0, historyManager.getHistory().size(), "История должна быть пустой");
        historyManager.remove(1);
        Task task1 = new Task("Task1", "Task1 description");
        Task task2 = new Task("Task2", "Task2 description");
        Task task3 = new Task("Task3", "Task3 description");
        Task task4 = new Task("Task4", "Task4 description");
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);
        assertNotNull(historyManager.getHistory());
        historyManager.remove(task1.getId()); //удаление из начала списка
        assertEquals(3, historyManager.getHistory().size());
        historyManager.remove(task3.getId()); //удаление из середины списка
        assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(task4.getId()); //удаление из конца списка
        assertEquals(1, historyManager.getHistory().size());

        historyManager.remove(455);  //удаление несуществующего таска
    }
}
