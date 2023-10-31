package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;
    protected HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }
    @Override
    public Collection<Subtask> getSubtasks() {
        return subtasks.values();
    }
    @Override
    public Collection<Epic> getEpics() {
        return epics.values();
    }
    @Override
    public void deleteAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }
    @Override
    public void deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
        // Обновляем статусы эпиков
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
        }
    }
    @Override
    public void deleteAllEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
        }
    }
    @Override
    public Task getTaskById(Integer id) {
        if (!tasks.isEmpty()) {
            Task task = tasks.get(id);
            historyManager.add(task);
            return task;
        }
        return null;
    }
    @Override
    public Task getSubtaskById(Integer id) {
        if (!subtasks.isEmpty()) {
            Subtask subtask = subtasks.get(id);
            historyManager.add(subtask);
            return subtask;
        }
        return null;
    }
    @Override
    public Task getEpicById(Integer id) {
        if (!epics.isEmpty()) {
            Epic epic = epics.get(id);
            historyManager.add(epic);
            return epic;
        }
        return null;
    }
    @Override
    public void createTask(Task task) {
        //Integer id = getCurrentId();
        //task.setId(id);
        tasks.put(task.getId(), task);
    }
    @Override
    public void createSubtask(Subtask subtask) {
        //Integer id = getCurrentId();
        //subtask.setId(id);
        subtasks.put(subtask.getId(), subtask);
        if (!epics.isEmpty()) {
            Epic epic = epics.get(subtask.getEpicId());
            // Обновляем статус эпика
            if (epic != null) {
                epic.getSubtasksId().add(subtask.getId());
                checkAndUpdateEpicStatus(epic);
                epics.put(epic.getId(), epic);
            }
        }
    }
    @Override
    public void createEpic(Epic epic) {
        //Integer id = getCurrentId();
        //epic.setId(id);
        // Обновляем статус эпика
        checkAndUpdateEpicStatus(epic);
        epics.put(epic.getId(), epic);
    }
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Ошибка обновления task = " + task.getId());
        }

    }
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            // Обновляем статус эпика
            checkAndUpdateEpicStatus(epic);
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Ошибка обновления subtask = " + subtask.getId());
        }

    }
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            // Обновляем статус эпика
            checkAndUpdateEpicStatus(epic);
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Ошибка обновления epic = " + epic.getId());
        }

    }
    @Override
    public void deleteTaskById(Integer id) {
        if (!tasks.isEmpty()) {
            tasks.remove(id);
        }
    }
    @Override
    public void deleteSubtaskById(Integer id) {
        if (!subtasks.isEmpty()) {
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            subtasks.remove(id);
            // Обновляем статус эпика
            checkAndUpdateEpicStatus(epic);
            epics.put(epic.getId(), epic);
        }
    }
    @Override
    public void deleteEpicById(Integer id) {
        if (!epics.isEmpty()) {
            ArrayList<Integer> subtasksId = epics.get(id).getSubtasksId();
            for (Integer subtaskId : subtasksId) {
                subtasks.remove(subtaskId); // Удаляем все сабтаски эпика
            }
            epics.remove(id);
        }
    }
    @Override
    public ArrayList<Subtask> getEpicsSubtasks(Epic epic) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        Integer epicId = epic.getId();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId().equals(epicId)) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Вычисляем и меняем статус эпика
    private void checkAndUpdateEpicStatus(Epic epic) {
        Boolean statusNew = true;
        Boolean statusDone = true;
        if (epic.getSubtasksId() == null || epic.getSubtasksId().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            for (Integer id : subtasksId) {
                Subtask subtask = subtasks.get(id);
                if (subtask != null) {
                    if (subtask.getStatus() != Status.NEW) {
                        statusNew = false;
                    }
                    if (subtask.getStatus() != Status.DONE) {
                        statusDone = false;
                    }
                }
            }
            if (statusNew) {
                epic.setStatus(Status.NEW);
            } else if (statusDone) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

}


