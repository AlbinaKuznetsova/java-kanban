import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Manager {
    protected Integer currentId; // текущий идентификатор, при создании новой задачи увеличивается на 1
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;

    public Manager() {
        this.currentId = 0;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public Integer getCurrentId() {
        currentId++;
        return currentId;
    }

    public Collection<Task> getTasks() {
        // Не смогла преобразовать tasks.values() в ArrayList, поэтому возвращаю Collection
        // И не поняла, почему храним задачи в мапе, а возвращаем список
        return tasks.values();
    }

    public Collection<Subtask> getSubtasks() {
        return subtasks.values();
    }

    public Collection<Epic> getEpics() {
        return epics.values();
    }

    public void deleteAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    public void deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
        // Обновляем статусы эпиков
        for (Epic epic : epics.values()) {
            epic.setStatus("NEW");
            epics.put(epic.id, epic);
        }
    }

    public void deleteAllEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }

    public Task getTaskById(Integer id) {
        if (!tasks.isEmpty()) {
            return tasks.get(id);
        }
        return null;
    }

    public Task getSubtaskById(Integer id) {
        if (!subtasks.isEmpty()) {
            return subtasks.get(id);
        }
        return null;
    }

    public Task getEpicById(Integer id) {
        if (!epics.isEmpty()) {
            return epics.get(id);
        }
        return null;
    }

    public void createTask(Task task) {
        Integer id = getCurrentId();
        //task.setId(id);
        tasks.put(id, task);
    }

    public void createSubtask(Subtask subtask) {
        Integer id = getCurrentId();
        //subtask.setId(id);
        subtasks.put(id, subtask);
        if (!epics.isEmpty()) {
            Epic epic = epics.get(subtask.getEpicId());
            // Обновляем статус эпика
            if (epic != null) {
                epic.getSubtasksId().add(id);
                checkAndUpdateEpicStatus(epic);
                epics.put(epic.id, epic);
            }
        }
    }

    public void createEpic(Epic epic) {
        Integer id = getCurrentId();
        //epic.setId(id);
        // Обновляем статус эпика
        checkAndUpdateEpicStatus(epic);
        epics.put(id, epic);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.id)) {
            tasks.put(task.id, task);
        } else {
            System.out.println("Ошибка обновления task = " + task.id);
        }

    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.id)) {
            subtasks.put(subtask.id, subtask);
            Epic epic = epics.get(subtask.getEpicId());
            // Обновляем статус эпика
            checkAndUpdateEpicStatus(epic);
            epics.put(epic.id, epic);
        } else {
            System.out.println("Ошибка обновления subtask = " + subtask.id);
        }

    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.id)) {
            // Обновляем статус эпика
            checkAndUpdateEpicStatus(epic);
            epics.put(epic.id, epic);
        } else {
            System.out.println("Ошибка обновления epic = " + epic.id);
        }

    }

    public void deleteTaskById(Integer id) {
        if (!tasks.isEmpty()) {
            tasks.remove(id);
        }
    }

    public void deleteSubtaskById(Integer id) {
        if (!subtasks.isEmpty()) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            subtasks.remove(id);
            // Обновляем статус эпика
            checkAndUpdateEpicStatus(epic);
            epics.put(epic.id, epic);
        }
    }

    public void deleteEpicById(Integer id) {
        if (!epics.isEmpty()) {
            epics.remove(id); // Непонятно по ТЗ, нужно ли при удалении эпика удалять все его сабтаски
        }
    }

    public ArrayList<Subtask> getEpicsSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        Integer epicId = epic.id;
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId().equals(epicId)) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    // Вычисляем и меняем статус эпика
    private void checkAndUpdateEpicStatus(Epic epic) {
        Boolean statusNew = true;
        Boolean statusDone = true;
        if (epic.getSubtasksId() == null || epic.getSubtasksId().isEmpty()) {
            epic.setStatus("NEW");
        } else {
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            for (Integer id : subtasksId) {
                Subtask subtask = subtasks.get(id);
                if (subtask != null) {
                    if (!subtask.getStatus().equals("NEW")) {
                        statusNew = false;
                    }
                    if (!subtask.getStatus().equals("DONE")) {
                        statusDone = false;
                    }
                }
            }
            if (statusNew) {
                epic.setStatus("NEW");
            } else if (statusDone) {
                epic.setStatus("DONE");
            } else {
                epic.setStatus("IN_PROGRESS");
            }
        }
    }


}


