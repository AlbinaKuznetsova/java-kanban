package managers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tasks.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskServer {
    private static TaskManager manager;
    private HttpServer httpServer;
    private KVServer kvServer = new KVServer();

    public HttpTaskServer() throws IOException {
        kvServer.start();

        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TaskManagerHandler());

        httpServer.start(); // запускаем сервер
        manager = Managers.getDefault();

    }

    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void stop() {
        httpServer.stop(0);
        kvServer.stop();
    }

    static class TaskManagerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента.");
            Gson gson = new Gson();
            String response = "";
            try {
                URI url = httpExchange.getRequestURI();
                String[] path = url.getPath().split("/");
                String method = httpExchange.getRequestMethod();
                String body = new String(httpExchange.getRequestBody().readAllBytes());
                int id = getIdFromURI(url);
                if (path.length == 2 && method.equals("GET")) {
                    response = gson.toJson(manager.getPrioritizedTasks());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("POST") && path[2].equals("task")) {
                    Task task = taskFromJson(path, body, id);
                    if (id != 0) { //Обновляем таск
                        System.out.println("Запрос на обновление таска " + id);
                        manager.updateTask(task);
                    } else {    //  Иначе создаем новый таск
                        System.out.println("Запрос на создание таска");
                        manager.createTask(task);
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("POST") && path[2].equals("subtask")) {
                    Subtask subtask = subtaskFromJson(path, body, id);
                    if (id != 0) { //Обновляем таск
                        System.out.println("Запрос на обновление сабтаска " + id);
                        manager.updateSubtask(subtask);
                    } else {    //  Иначе создаем новый таск
                        System.out.println("Запрос на создание сабтаска");
                        manager.createSubtask(subtask);
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("POST") && path[2].equals("epic")) {
                    Epic epic = epicFromJson(path, body, id);
                    if (id != 0) { //Обновляем таск
                        System.out.println("Запрос на обновление эпика " + id);
                        manager.updateEpic(epic);
                    } else {    //  Иначе создаем новый таск
                        System.out.println("Запрос на создание эпика");
                        manager.createEpic(epic);
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("GET") && path[2].equals("history")) { //Получение истории
                    System.out.println("Запрос на получение истории");
                    response = gson.toJson(manager.getHistory());
                    httpExchange.sendResponseHeaders(200, 0);

                } else if (path.length == 4 && method.equals("GET") && path[2].equals("subtask") && path[3].equals("epic")) {
                    // Получение сабтасков эпика
                    System.out.println("Запрос на получение сабтасков эпика");
                    response = gson.toJson(manager.getEpicsSubtasks(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("GET") && path[2].equals("task") && (id == 0)) {
                    System.out.println("Запрос на получение всех тасков");
                    response = gson.toJson(manager.getTasks());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("GET") && path[2].equals("subtask") && (id == 0)) {
                    System.out.println("Запрос на получение всех сабтасков");
                    response = gson.toJson(manager.getSubtasks());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("GET") && path[2].equals("epic") && (id == 0)) {
                    System.out.println("Запрос на получение всех эпиков");
                    response = gson.toJson(manager.getEpics());
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("GET") && path[2].equals("task") && (id != 0)) {
                    System.out.println("Запрос на получение конкретного таска");
                    response = gson.toJson(manager.getTaskById(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("GET") && path[2].equals("subtask") && (id != 0)) {
                    System.out.println("Запрос на получение конкретного сабтаска");
                    response = gson.toJson(manager.getSubtaskById(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("GET") && path[2].equals("epic") && (id != 0)) {
                    System.out.println("Запрос на получение конкретного эпика");
                    response = gson.toJson(manager.getEpicById(id));
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("DELETE") && path[2].equals("task") && (id == 0)) {
                    System.out.println("Запрос на удаление всех тасков");
                    manager.deleteAllTasks();
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("DELETE") && path[2].equals("subtask") && (id == 0)) {
                    System.out.println("Запрос на удаление всех сабтасков");
                    manager.deleteAllSubtasks();
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("DELETE") && path[2].equals("epic") && (id == 0)) {
                    System.out.println("Запрос на удаление всех эпиков");
                    manager.deleteAllEpics();
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("DELETE") && path[2].equals("task") && (id != 0)) {
                    System.out.println("Запрос на удаление таска");
                    manager.deleteTaskById(id);
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("DELETE") && path[2].equals("subtask") && (id != 0)) {
                    System.out.println("Запрос на удаление сабтаска");
                    manager.deleteSubtaskById(id);
                    httpExchange.sendResponseHeaders(200, 0);
                } else if (path.length == 3 && method.equals("DELETE") && path[2].equals("epic") && (id != 0)) {
                    System.out.println("Запрос на удаление эпика");
                    manager.deleteEpicById(id);
                    httpExchange.sendResponseHeaders(200, 0);
                }
            } catch (Exception ex) {
                httpExchange.sendResponseHeaders(500, 0);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(ex.getMessage().getBytes());
                }
            }


            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private int getIdFromURI(URI url) {
            int id = 0;
            String param = url.getQuery();
            if (param != null) {
                if (param.contains("id=")) {
                    id = Integer.parseInt(param.substring(param.indexOf("id=") + 3));
                }
            }
            return id;
        }

        private Task taskFromJson(String[] path, String body, int id) {
            Task task;
            String name = "";
            String description = "";
            String status = "";
            int duration = 0;
            LocalDateTime startTime = null;
            JsonElement element = JsonParser.parseString(body);
            JsonObject taskObject = null;
            if (element.isJsonObject()) {
                taskObject = element.getAsJsonObject();
            }
            if (id == 0) {
                if (taskObject.get("id") != null) {
                    id = taskObject.get("id").getAsInt();
                }
            }
            if (taskObject.get("name") != null) {
                name = taskObject.get("name").getAsString();
            }
            if (taskObject.get("description") != null) {
                description = taskObject.get("description").getAsString();
            }
            if (taskObject.get("status") != null) {
                status = taskObject.get("status").getAsString();
            }
            if (taskObject.get("duration") != null) {
                duration = taskObject.get("duration").getAsInt();
            }
            if (taskObject.get("startTime") != null) {
                JsonObject date = taskObject.get("startTime").getAsJsonObject().get("date").getAsJsonObject();
                JsonObject time = taskObject.get("startTime").getAsJsonObject().get("time").getAsJsonObject();
                startTime = LocalDateTime.of(date.get("year").getAsInt(), date.get("month").getAsInt(), date.get("day").getAsInt()
                        , time.get("hour").getAsInt(), time.get("minute").getAsInt(), time.get("second").getAsInt());
            }
            if (id != 0) {
                task = new Task(name, description, id, Status.valueOf(status), duration, startTime);
            } else {
                task = new Task(name, description, duration, startTime);
            }
            return task;
        }

        private Epic epicFromJson(String[] path, String body, int id) {
            Epic epic;
            String name = "";
            String description = "";
            String status = "";
            int duration = 0;
            LocalDateTime startTime = null;
            ArrayList<Integer> subtasksId = new ArrayList<>();
            JsonElement element = JsonParser.parseString(body);
            JsonObject taskObject = null;
            if (element.isJsonObject()) {
                taskObject = element.getAsJsonObject();
            }
            if (id == 0) {
                if (taskObject.get("id") != null) {
                    id = taskObject.get("id").getAsInt();
                }
            }
            if (taskObject.get("name") != null) {
                name = taskObject.get("name").getAsString();
            }
            if (taskObject.get("description") != null) {
                description = taskObject.get("description").getAsString();
            }
            if (taskObject.get("status") != null) {
                status = taskObject.get("status").getAsString();
            }
            if (taskObject.get("duration") != null) {
                duration = taskObject.get("duration").getAsInt();
            }
            if (taskObject.get("subtasksId") != null) {
                for (int i = 0; i < taskObject.get("subtasksId").getAsJsonArray().size(); i++) {
                    subtasksId.add(taskObject.get("subtasksId").getAsJsonArray().get(i).getAsInt());
                }
            }
            if (taskObject.get("startTime") != null) {
                JsonObject date = taskObject.get("startTime").getAsJsonObject().get("date").getAsJsonObject();
                JsonObject time = taskObject.get("startTime").getAsJsonObject().get("time").getAsJsonObject();
                startTime = LocalDateTime.of(date.get("year").getAsInt(), date.get("month").getAsInt(), date.get("day").getAsInt()
                        , time.get("hour").getAsInt(), time.get("minute").getAsInt(), time.get("second").getAsInt());
            }
            if (id != 0) {
                epic = new Epic(name, description, id, Status.valueOf(status), duration, startTime, subtasksId);
            } else {
                epic = new Epic(name, description);
            }
            return epic;
        }

        private Subtask subtaskFromJson(String[] path, String body, int id) {
            Subtask subtask;
            String name = "";
            String description = "";
            String status = "";
            int duration = 0;
            int epicId = 0;
            LocalDateTime startTime = null;
            JsonElement element = JsonParser.parseString(body);
            JsonObject taskObject = null;
            if (element.isJsonObject()) {
                taskObject = element.getAsJsonObject();
            }
            if (id == 0) {
                if (taskObject.get("id") != null) {
                    id = taskObject.get("id").getAsInt();
                }
            }
            if (taskObject.get("name") != null) {
                name = taskObject.get("name").getAsString();
            }
            if (taskObject.get("description") != null) {
                description = taskObject.get("description").getAsString();
            }
            if (taskObject.get("status") != null) {
                status = taskObject.get("status").getAsString();
            }
            if (taskObject.get("duration") != null) {
                duration = taskObject.get("duration").getAsInt();
            }
            if (taskObject.get("epicId") != null) {
                epicId = taskObject.get("epicId").getAsInt();
            }
            if (taskObject.get("startTime") != null) {
                JsonObject date = taskObject.get("startTime").getAsJsonObject().get("date").getAsJsonObject();
                JsonObject time = taskObject.get("startTime").getAsJsonObject().get("time").getAsJsonObject();
                startTime = LocalDateTime.of(date.get("year").getAsInt(), date.get("month").getAsInt(), date.get("day").getAsInt()
                        , time.get("hour").getAsInt(), time.get("minute").getAsInt(), time.get("second").getAsInt());
            }
            if (id != 0) {
                subtask = new Subtask(name, description, epicId, id, Status.valueOf(status), duration, startTime);
            } else {
                subtask = new Subtask(name, description, epicId, duration, startTime);
            }
            return subtask;
        }
    }


}
