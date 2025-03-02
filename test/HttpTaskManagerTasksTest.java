import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import tracker.model.*;
import tracker.server.HttpTaskServer;
import tracker.service.Managers;
import tracker.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.TypeOfTask.NEW;

public class HttpTaskManagerTasksTest {

    HttpTaskServer taskServer = new HttpTaskServer();
    TaskManager manager = HttpTaskServer.getInMemoryTaskManager();
    Gson gson = Managers.getDefaultGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачи
        Task task = new Task("TaskTime", "Первая 11:00 - 11:10"
                ,NEW, "2021-12-20T11:00:00", 10L);
        Task taskNot = new Task("TaskTime2", "Первая 11:05 - 11:15"
                ,NEW, "2021-12-20T11:00:00", 10L);
        String taskJson = gson.toJson(task);
        String taskJsonNot = gson.toJson(taskNot);

        //------------- создаём HTTP-клиент и запрос POST-----------------
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());  // проверяем код ответа

        //------------- запрос POST на пересечение-----------------
        HttpRequest requestNot = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJsonNot)).build();
        HttpResponse<String> responseNot = client.send(requestNot, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, responseNot.statusCode());  // проверяем код ответа

        //-------------------запрос GET всех задач--------------------------
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());  // проверяем код ответа
        // Десериализация в массив тасков
        Task[] taskDeserialized = gson.fromJson(response.body(), Task[].class);
        assertNotNull(taskDeserialized, "Задачи не возвращаются");
        assertEquals(1, taskDeserialized.length, "Некорректное количество задач");
        assertEquals(task.getTaskDetails(),  taskDeserialized[0].getTaskDetails(), "Задачи не совпадают");

        //-------------------запрос GET по валидному айди--------------------------
        url = URI.create("http://localhost:8080/tasks/0");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());  // проверяем код ответа

        //-------------------запрос GET по Невалидному айди--------------------------
        url = URI.create("http://localhost:8080/tasks/5");
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());  // проверяем код ответа

        //-------------------запрос DELETE --------------------------
        url = URI.create("http://localhost:8080/tasks");
        request = HttpRequest.newBuilder().uri(url).DELETE().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());  // проверяем код ответа
        request = HttpRequest.newBuilder().uri(url).GET().build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        taskDeserialized = gson.fromJson(response.body(), Task[].class);
        assertEquals(0,taskDeserialized.length, "Задачи возвращаются");
    }
}
