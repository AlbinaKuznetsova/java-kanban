package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVFormat {
    public static String toString(Task task) {
        TaskType type = null;
        String epicId = "";
        if (task.getType() == TaskType.SUBTASK) {
            epicId = ((Subtask) task).getEpicId().toString();
        }
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + epicId;
    }

    public static String historyToString(HistoryManager manager) {
        String historyIds = "";
        List<Task> history = manager.getHistory();

        if (!history.isEmpty()) {
            for (Task task : history) {
                historyIds += task.getId() + ",";
            }
        }
        return historyIds;
    }

    public static Task fromString(String value) {
        String[] values = value.split(",");
        Task task = null;
        if (values[1].equals(TaskType.TASK.toString())) {
            task = new Task(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]));
        } else if (values[1].equals(TaskType.SUBTASK.toString())) {
            task = new Subtask(values[2], values[4], Integer.parseInt(values[5]), Integer.parseInt(values[0]), Status.valueOf(values[3]));
        } else if (values[1].equals(TaskType.EPIC.toString())) {
            task = new Epic(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]));
        }
        return task;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        String[] historyIds = value.split(",");
        for (String historyId : historyIds) {
            if (!historyId.isBlank()) {
                history.add(Integer.parseInt(historyId));
            }
        }
        Collections.reverse(history);
        return history;
    }
}
