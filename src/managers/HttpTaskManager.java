package managers;


import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private Gson gson;

    public HttpTaskManager(String serverUrl) {
        super();
        client = new KVTaskClient(serverUrl);
        gson = new Gson();
    }

    private HttpTaskManager(KVTaskClient client) {
        super();
        this.client = client;
        gson = new Gson();
    }

    public KVTaskClient getClient() {
        return client;
    }

    @Override
    protected void save() { // метод сохраняет информацию из менеджера на сервер
        try {
            String tasksJson = gson.toJson(getTasks());
            client.put("tasks", tasksJson);
            String subtasksJson = gson.toJson(getSubtasks());
            client.put("subtasks", subtasksJson);
            String epicsJson = gson.toJson(getEpics());
            client.put("epics", epicsJson);
            String historyJson = gson.toJson(CSVFormat.historyToString(historyManager));
            client.put("history", historyJson);
        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
    }

    public static HttpTaskManager load(KVTaskClient client) {
        HttpTaskManager manager = new HttpTaskManager(client);
        try {
            ArrayList<Task> tasksFromJson = manager.gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>() {
            }.getType());
            for (Task task : tasksFromJson) {
                manager.addTask(task);
            }
            ArrayList<Subtask> subtasksFromJson = manager.gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<Subtask>>() {
            }.getType());
            for (Subtask subtask : subtasksFromJson) {
                manager.addTask(subtask);
            }

            ArrayList<Epic> epicsFromJson = manager.gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>() {
            }.getType());
            for (Epic epic : epicsFromJson) {
                manager.addTask(epic);
            }

            String historyIdsString = manager.gson.fromJson(client.load("history"), String.class);

            for (Integer id : CSVFormat.historyFromString(historyIdsString)) {
                manager.getTask(id);
            }

        } catch (Exception exp) {
            System.out.println(exp.getMessage());
        }
        return manager;
    }
}
