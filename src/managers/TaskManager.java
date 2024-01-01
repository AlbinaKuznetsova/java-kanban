package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    List<Task> getTasks();
    List<Subtask> getSubtasks();
    List<Epic> getEpics();
    void deleteAllTasks();
    void deleteAllSubtasks();
    void deleteAllEpics();
    Task getTaskById(Integer id);
    Task getSubtaskById(Integer id);
    Task getEpicById(Integer id);
    void createTask(Task task);
    void createSubtask(Subtask subtask);
    void createEpic(Epic epic);
    void updateTask(Task task);
    void updateSubtask(Subtask subtask);
    void updateEpic(Epic epic);
    void deleteTaskById(Integer id);
    void deleteSubtaskById(Integer id);
    void deleteEpicById(Integer id);
    ArrayList<Subtask> getEpicsSubtasks(Epic epic);
    List<Task> getHistory();
    TreeSet<Task> getPrioritizedTasks();
}
