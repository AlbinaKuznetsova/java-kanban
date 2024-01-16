package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import managers.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskServerTest {
    HttpTaskServer server;
    String url = "http://localhost:8080/tasks/";
    Gson gson = new Gson();
    Task task1;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void beforeEach() throws Exception {
        try {
            server = new HttpTaskServer();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        task1 = new Task("task1", "Купить автомобиль", 100
                , LocalDateTime.of(2023, Month.DECEMBER, 1, 12, 0, 0));
        epic1 = new Epic("new Epic1", "Новый Эпик");
        subtask1 = new Subtask("New Subtask", "Подзадача", epic1.getId(), 30
                , LocalDateTime.of(2023, Month.DECEMBER, 19, 12, 0, 0));
        subtask2 = new Subtask("New Subtask2", "Подзадача2", epic1.getId(), 180
                , LocalDateTime.of(2023, Month.DECEMBER, 20, 10, 0, 0));
        // Создаем таск
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(task1));
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(URI.create(url + "task/")).build();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Создаем эпик
        body = HttpRequest.BodyPublishers.ofString(gson.toJson(epic1));
        request = HttpRequest.newBuilder().POST(body).uri(URI.create(url + "epic/")).build();
        client = HttpClient.newHttpClient();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        // Создаем сабтаски
        body = HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1));
        request = HttpRequest.newBuilder().POST(body).uri(URI.create(url + "subtask/")).build();
        client = HttpClient.newHttpClient();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        body = HttpRequest.BodyPublishers.ofString(gson.toJson(subtask2));
        request = HttpRequest.newBuilder().POST(body).uri(URI.create(url + "subtask/")).build();
        client = HttpClient.newHttpClient();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @AfterEach
    public void afterEach() {
        server.stop();
    }

    @Test
    void createTask() throws Exception {
        URI uri = URI.create(url + "task/");
        Task task = new Task("Task test", "Task test description", 120
                , LocalDateTime.of(2023, Month.MAY, 20, 10, 0, 0));
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(task));
        HttpRequest request1 = HttpRequest.newBuilder().POST(body1).uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        assertEquals(200, response1.statusCode());

        URI getUri = URI.create(url + "task/?id=" + task.getId());

        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(getUri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        assertEquals(task, gson.fromJson(response2.body(), Task.class), "Задачи не совпадают.");
    }

    @Test
    void createSubtask() throws Exception {
        URI uri = URI.create(url + "subtask/");
        Subtask subtask = new Subtask("Task test", "Task test description", epic1.getId(), 60
                , LocalDateTime.of(2023, Month.DECEMBER, 12, 12, 0, 0));
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(subtask));
        HttpRequest request1 = HttpRequest.newBuilder().POST(body1).uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        assertEquals(200, response1.statusCode());

        URI getUri = URI.create(url + "subtask/?id=" + subtask.getId());

        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(getUri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        assertEquals(subtask, gson.fromJson(response2.body(), Subtask.class), "Задачи не совпадают.");

    }

    @Test
    void createEpic() throws Exception {
        URI uri = URI.create(url + "epic/");
        Epic epic = new Epic("new Epic test", "Новый Эпик");
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(epic));
        HttpRequest request1 = HttpRequest.newBuilder().POST(body1).uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        assertEquals(200, response1.statusCode());

        URI getUri = URI.create(url + "epic/?id=" + epic.getId());

        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(getUri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        assertEquals(epic, gson.fromJson(response2.body(), Epic.class), "Задачи не совпадают.");
    }

    @Test
    void getTasks() throws Exception {
        URI uri = URI.create(url + "task/");
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        ArrayList<Task> tasksFromJson = gson.fromJson(response2.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(1, tasksFromJson.size(), "Задачи не совпадают.");
        assertEquals(task1, tasksFromJson.get(0), "Задачи не совпадают.");
    }

    @Test
    void getSubtasks() throws Exception {
        URI uri = URI.create(url + "subtask/");
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        ArrayList<Subtask> subtasksFromJson = gson.fromJson(response2.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(2, subtasksFromJson.size(), "Задачи не совпадают.");
        assertTrue(subtasksFromJson.contains(subtask1), "Задачи не совпадают.");
        assertTrue(subtasksFromJson.contains(subtask2), "Задачи не совпадают.");
    }

    @Test
    void getEpics() throws Exception {
        URI uri = URI.create(url + "epic/");
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        ArrayList<Epic> epicsFromJson = gson.fromJson(response2.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        assertEquals(1, epicsFromJson.size(), "Задачи не совпадают.");
        assertEquals(epic1.getId(), epicsFromJson.get(0).getId(), "Задачи не совпадают.");
    }

    @Test
    void getTaskById() throws Exception {
        URI uri = URI.create(url + "task/?id=" + task1.getId());
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        Task taskFromJson = gson.fromJson(response2.body(), Task.class);

        assertEquals(task1, taskFromJson, "Задачи не совпадают.");
    }

    @Test
    void getSubtaskById() throws Exception {
        URI uri = URI.create(url + "subtask/?id=" + subtask1.getId());
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        Subtask subtaskFromJson = gson.fromJson(response2.body(), Subtask.class);

        assertEquals(subtask1, subtaskFromJson, "Задачи не совпадают.");
    }

    @Test
    void getEpicById() throws Exception {
        URI uri = URI.create(url + "epic/?id=" + epic1.getId());
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        Epic epicFromJson = gson.fromJson(response2.body(), Epic.class);

        assertEquals(epic1.getId(), epicFromJson.getId(), "Задачи не совпадают.");
    }

    @Test
    void deleteAllTasks() throws Exception {
        URI uri = URI.create(url + "task/");
        HttpRequest request2 = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        // Проверим, что возвращается пустой список задач
        HttpResponse<String> response1 = client1.send(request1, handler1);
        ArrayList<Task> tasksFromJson = gson.fromJson(response1.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(0, tasksFromJson.size(), "Задачи не совпадают.");
    }

    @Test
    void deleteAllSubtasks() throws Exception {
        URI uri = URI.create(url + "subtask/");
        HttpRequest request2 = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        // Проверим, что возвращается пустой список задач
        HttpResponse<String> response1 = client1.send(request1, handler1);
        ArrayList<Subtask> subtasksFromJson = gson.fromJson(response1.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(0, subtasksFromJson.size(), "Задачи не совпадают.");
    }

    @Test
    void deleteAllEpics() throws Exception {
        URI uri = URI.create(url + "epic/");
        HttpRequest request2 = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        // Проверим, что возвращается пустой список задач
        HttpResponse<String> response1 = client1.send(request1, handler1);
        ArrayList<Epic> epicsFromJson = gson.fromJson(response1.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        assertEquals(0, epicsFromJson.size(), "Задачи не совпадают.");
    }

    @Test
    void deleteTaskById() throws Exception {
        URI uri = URI.create(url + "task/?id=" + task1.getId());
        HttpRequest request2 = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(URI.create(url + "task/")).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        // Проверим, что возвращается пустой список задач
        HttpResponse<String> response1 = client1.send(request1, handler1);
        ArrayList<Task> tasksFromJson = gson.fromJson(response1.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(0, tasksFromJson.size(), "Задачи не совпадают.");
    }

    @Test
    void deleteSubtaskById() throws Exception {
        URI uri = URI.create(url + "subtask/?id=" + subtask1.getId());
        HttpRequest request2 = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(URI.create(url + "subtask/")).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        // Проверим, что возвращается пустой список задач
        HttpResponse<String> response1 = client1.send(request1, handler1);
        ArrayList<Subtask> subtasksFromJson = gson.fromJson(response1.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(1, subtasksFromJson.size(), "Задачи не совпадают.");
    }

    @Test
    void deleteEpicById() throws Exception {
        URI uri = URI.create(url + "epic/?id=" + epic1.getId());
        HttpRequest request2 = HttpRequest.newBuilder().DELETE().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder().GET().uri(URI.create(url + "epic/")).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        // Проверим, что возвращается пустой список задач
        HttpResponse<String> response1 = client1.send(request1, handler1);
        ArrayList<Epic> epicsFromJson = gson.fromJson(response1.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        assertEquals(0, epicsFromJson.size(), "Задачи не совпадают.");

        // Проверяем, что удалились сабтаски при удалении эпика
        request1 = HttpRequest.newBuilder().GET().uri(URI.create(url + "subtask/")).build();
        client1 = HttpClient.newHttpClient();
        handler1 = HttpResponse.BodyHandlers.ofString();

        // Проверим, что возвращается пустой список задач
        response1 = client1.send(request1, handler1);
        ArrayList<Subtask> subtasksFromJson = gson.fromJson(response1.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(0, subtasksFromJson.size(), "Задачи не совпадают.");
    }

    @Test
    void updateTask() throws Exception {
        URI uri = URI.create(url + "task/?id=" + task1.getId());
        task1.setStatus(Status.DONE);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(task1));
        HttpRequest request1 = HttpRequest.newBuilder().POST(body1).uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        assertEquals(200, response1.statusCode());

        URI getUri = URI.create(url + "task/?id=" + task1.getId());

        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(getUri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        assertEquals(task1, gson.fromJson(response2.body(), Task.class), "Задачи не совпадают.");

    }

    @Test
    void updateSubtask() throws Exception {
        URI uri = URI.create(url + "subtask/?id=" + subtask1.getId());
        subtask1.setStatus(Status.IN_PROGRESS);
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(subtask1));
        HttpRequest request1 = HttpRequest.newBuilder().POST(body1).uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        assertEquals(200, response1.statusCode());

        URI getUri = URI.create(url + "subtask/?id=" + subtask1.getId());

        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(getUri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        assertEquals(subtask1, gson.fromJson(response2.body(), Subtask.class), "Задачи не совпадают.");

    }

    @Test
    void updateEpic() throws Exception {
        URI uri = URI.create(url + "epic/?id=" + epic1.getId());
        epic1.setDescription("Проверяем обновление эпика");
        HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(gson.toJson(epic1));
        HttpRequest request1 = HttpRequest.newBuilder().POST(body1).uri(uri).build();
        HttpClient client1 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler1 = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> response1 = client1.send(request1, handler1);
        assertEquals(200, response1.statusCode());

        URI getUri = URI.create(url + "epic/?id=" + epic1.getId());

        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(getUri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        assertEquals(epic1, gson.fromJson(response2.body(), Epic.class), "Задачи не совпадают.");
    }

    @Test
    void getEpicsSubtasks() throws Exception {
        URI uri = URI.create(url + "subtask/epic/?id=" + epic1.getId());
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        ArrayList<Subtask> subtasksFromJson = gson.fromJson(response2.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(2, subtasksFromJson.size(), "Задачи не совпадают.");
        assertTrue(subtasksFromJson.contains(subtask1), "Задачи не совпадают.");
        assertTrue(subtasksFromJson.contains(subtask2), "Задачи не совпадают.");
    }

    @Test
    void getPrioritizedTasks() throws Exception {
        URI uri = URI.create(url);
        HttpRequest request2 = HttpRequest.newBuilder().GET().uri(uri).build();
        HttpClient client2 = HttpClient.newHttpClient();
        HttpResponse.BodyHandler<String> handler2 = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response2 = client2.send(request2, handler2);
        assertEquals(200, response2.statusCode());
        ArrayList<Task> tasksFromJson = gson.fromJson(response2.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(3, tasksFromJson.size(), "Задачи не совпадают.");
        assertEquals(task1.getId(), tasksFromJson.get(0).getId(), "Задачи не совпадают.");
        assertEquals(subtask1.getId(), tasksFromJson.get(1).getId(), "Задачи не совпадают.");
        assertEquals(subtask2.getId(), tasksFromJson.get(2).getId(), "Задачи не совпадают.");
    }
}
