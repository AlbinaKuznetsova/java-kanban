package managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private String url;

    public KVTaskClient(String url) {
        this.url = url;
        URI uri = URI.create(url + "register");
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpResponse<String> response = client.send(request, handler);

            if (response.statusCode() == 200) {
                this.apiToken = response.body();
            }
        } catch (Exception ex) {
            this.apiToken = "";
            System.out.println(ex.getMessage());
        }
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken);
        try {
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpResponse<String> response = client.send(request, handler);

            if (response.statusCode() != 200) {
                System.out.println("Возникла ошибка, код ответа сервера - " + response.statusCode());
            }
        } catch (Exception ex) {
            this.apiToken = "";
            System.out.println(ex.getMessage());
        }
    }

    public String load(String key) {
        String value = "";
        URI uri = URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken);
        try {
            HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            HttpResponse<String> response = client.send(request, handler);

            if (response.statusCode() != 200) {
                System.out.println("Возникла ошибка, код ответа сервера - " + response.statusCode());
            } else {
                value = response.body();
            }
        } catch (Exception ex) {
            this.apiToken = "";
            System.out.println(ex.getMessage());
        }
        return value;
    }
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        KVTaskClient client = new KVTaskClient("http://localhost:8078/");
        client.put("epic", "{\"name\": \"new Epic1\", \"description\": \"Новый Эпик\"}");
        client.put("task", "{\"name\": \"new task1\", \"description\": \"Новый task\"}");
        System.out.println(client.load("epic"));
        System.out.println(client.load("task"));
        client.put("epic", "{\"name\": \"new Epic11213243\", \"description\": \"Новый Эпик\"}");
        System.out.println(client.load("epic"));
        System.out.println(client.load("task"));
    }
}
