package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Subtask> subtasks;
    protected HashMap<Integer, Epic> epics;
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected TreeSet<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        TaskComparator taskComparator = new TaskComparator();
        this.prioritizedTasks = new TreeSet<>(taskComparator);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllTasks() {
        if (!tasks.isEmpty()) {
            for (Integer taskId : tasks.keySet()) {
                prioritizedTasks.remove(tasks.get(taskId));
                historyManager.remove(taskId);
            }
            tasks.clear();
        }
    }

    @Override
    public void deleteAllSubtasks() {
        if (!subtasks.isEmpty()) {
            for (Integer subtaskId : subtasks.keySet()) {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                historyManager.remove(subtaskId);
            }
            subtasks.clear();
        }
        // Обновляем статусы эпиков
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteAllEpics() {
        if (!epics.isEmpty()) {
            for (Integer epicId : epics.keySet()) {
                ArrayList<Integer> subtasksId = epics.get(epicId).getSubtasksId();
                for (Integer subtaskId : subtasksId) {
                    prioritizedTasks.remove(subtasks.get(subtaskId));
                    subtasks.remove(subtaskId); // Удаляем все сабтаски эпика
                    historyManager.remove(subtaskId);
                }
                historyManager.remove(epicId);
            }
            epics.clear();
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        if (!tasks.isEmpty()) {
            Task task = tasks.get(id);
            if (task != null) {
                historyManager.add(task);
            }
            return task;
        }
        return null;
    }

    @Override
    public Task getSubtaskById(Integer id) {
        if (!subtasks.isEmpty()) {
            Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                historyManager.add(subtask);
            }
            return subtask;
        }
        return null;
    }

    @Override
    public Task getEpicById(Integer id) {
        if (!epics.isEmpty()) {
            Epic epic = epics.get(id);
            if (epic != null) {
                historyManager.add(epic);
            }
            return epic;
        }
        return null;
    }

    @Override
    public void createTask(Task task) {
        if (!checkTasksOverlap(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        } else {
            System.out.println("Задачи не могут пересекаться по времени");
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (!checkTasksOverlap(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            prioritizedTasks.add(subtask);
            if (!epics.isEmpty()) {
                Epic epic = epics.get(subtask.getEpicId());
                // Обновляем статус эпика
                if (epic != null) {
                    epic.getSubtasksId().add(subtask.getId());
                    checkAndUpdateEpicStatus(epic);
                    updateEpicStartTime(epic);
                    epics.put(epic.getId(), epic);
                }
            }
        } else {
            System.out.println("Задачи не могут пересекаться по времени");
        }
    }

    @Override
    public void createEpic(Epic epic) {
        // Обновляем статус эпика
        checkAndUpdateEpicStatus(epic);
        updateEpicStartTime(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            prioritizedTasks.remove(tasks.get(task.getId()));
            if (!checkTasksOverlap(task)) {
                prioritizedTasks.add(task);
                tasks.put(task.getId(), task);
            } else {
                System.out.println("Задачи не могут пересекаться по времени");
            }
        } else {
            throw new NullPointerException();
        }

    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            prioritizedTasks.remove(subtasks.get(subtask.getId()));
            if (!checkTasksOverlap(subtask)) {

                prioritizedTasks.add(subtask);
                subtasks.put(subtask.getId(), subtask);
                Epic epic = epics.get(subtask.getEpicId());
                // Обновляем статус эпика
                checkAndUpdateEpicStatus(epic);
                updateEpicStartTime(epic);
                epics.put(epic.getId(), epic);
            } else {
                System.out.println("Задачи не могут пересекаться по времени");
            }
        } else {
            throw new NullPointerException();
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            // Обновляем статус эпика
            checkAndUpdateEpicStatus(epic);
            updateEpicStartTime(epic);
            epics.put(epic.getId(), epic);
        } else {
            throw new NullPointerException();
        }

    }

    @Override
    public void deleteTaskById(Integer id) {
        if (tasks.get(id) != null) {
            prioritizedTasks.remove(tasks.get(id));
        }
        if (!tasks.isEmpty()) {
            tasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (!subtasks.isEmpty()) {
            if (subtasks.get(id) != null) {
                prioritizedTasks.remove(subtasks.get(id));
            }
            Epic epic = epics.get(subtasks.get(id).getEpicId());
            subtasks.remove(id);
            historyManager.remove(id);
            // Обновляем статус эпика
            epic.getSubtasksId().remove(id);
            checkAndUpdateEpicStatus(epic);
            updateEpicStartTime(epic);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (!epics.isEmpty()) {
            ArrayList<Integer> subtasksId = epics.get(id).getSubtasksId();
            for (Integer subtaskId : subtasksId) {
                prioritizedTasks.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId); // Удаляем все сабтаски эпика
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public ArrayList<Subtask> getEpicsSubtasks(Integer epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
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

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }

    // Вычисляем и меняем статус эпика
    private void checkAndUpdateEpicStatus(Epic epic) {
        boolean statusNew = true;
        boolean statusDone = true;
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

    private void updateEpicStartTime(Epic epic) {
        if (epic.getSubtasksId() == null || epic.getSubtasksId().isEmpty()) {
            epic.setDuration(0);
        } else {
            int duration = 0;
            LocalDateTime endTime = null;
            LocalDateTime startTime = null;
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            for (int i = 0; i < subtasksId.size(); i++) {
                Subtask subtask = subtasks.get(subtasksId.get(i));
                if (subtask != null) {
                    duration += subtask.getDuration();
                    if (i == 0) {
                        startTime = subtask.getStartTime();
                        endTime = subtask.getEndTime();
                    } else {
                        if (subtask.getStartTime() != null && subtask.getEndTime() != null && startTime != null && endTime != null) {
                            if (subtask.getStartTime().isBefore(startTime)) {
                                startTime = subtask.getStartTime();
                            }
                            if (subtask.getEndTime().isAfter(endTime)) {
                                endTime = subtask.getEndTime();
                            }
                        }
                    }

                }
            }
            epic.setDuration(duration);
            epic.setStartTime(startTime);
            epic.setEndTime(endTime);
        }
    }

    private boolean checkTasksOverlap(Task newTask) { // Проверяем, не пересекается ли время выполнения задач,
        //Если пересекаются, возвращаем true
        boolean flag = false; // Флаг показывает, что задачи не пересекаются, иначе = true
        Iterator<Task> iterator = prioritizedTasks.iterator();
        while (!flag && iterator.hasNext()) {
            Task task = iterator.next();
            LocalDateTime startNewTask = newTask.getStartTime();
            LocalDateTime startTask = task.getStartTime();
            LocalDateTime endNewTask = newTask.getEndTime();
            LocalDateTime endTask = task.getEndTime();
            if (startNewTask != null && startTask != null && endNewTask != null && endTask != null) {
                if (startNewTask.isBefore(startTask)) {
                    if (endNewTask.isBefore(startTask)) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                } else {
                    if (startNewTask.isAfter(endTask)) {
                        flag = false;
                    } else {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

}


