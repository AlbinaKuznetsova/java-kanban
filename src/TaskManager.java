import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface TaskManager {
    Collection<Task> getTasks();
    Collection<Subtask> getSubtasks();
    Collection<Epic> getEpics();
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
}
