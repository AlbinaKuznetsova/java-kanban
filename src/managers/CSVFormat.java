package managers;

import tasks.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVFormat {
    public static String toString(Task task) {
        TaskType type = null;
        String epicId = "";
        if (task.getType() == TaskType.SUBTASK) {
            try {
                epicId = ((Subtask) task).getEpicId().toString();
            } catch (NullPointerException exp) {

            }
        }
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getDuration() + "," + task.getStartTime() + "," + epicId;
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
        LocalDateTime startTime = null;
        try {
            startTime = LocalDateTime.parse(values[6]);
        } catch (Exception ex) {

        }
        if (values[1].equals(TaskType.TASK.toString())) {
            task = new Task(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]), Integer.parseInt(values[5]), startTime);
        } else if (values[1].equals(TaskType.SUBTASK.toString())) {
            task = new Subtask(values[2], values[4], Integer.parseInt(values[7]), Integer.parseInt(values[0]), Status.valueOf(values[3]), Integer.parseInt(values[5]), startTime);
        } else if (values[1].equals(TaskType.EPIC.toString())) {
            task = new Epic(values[2], values[4], Integer.parseInt(values[0]), Status.valueOf(values[3]) ,Integer.parseInt(values[5]), startTime);
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
