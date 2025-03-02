package tracker.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.model.Epic;
import tracker.server.HttpTaskServer;
import tracker.server.data.BaseHttpHandler;
import tracker.service.InMemoryTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HandlerEpics implements HttpHandler {

    static Gson gson = HttpTaskServer.getGson();
    static InMemoryTaskManager inMemoryTaskManager = HttpTaskServer.getInMemoryTaskManager();
    static BaseHttpHandler baseHttpHandler = new BaseHttpHandler();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // метод из запроса
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        switch (method) {
            case "POST":
                handlePostRequest(httpExchange);
                break;
            case "GET":
                if (Pattern.matches("^/epics$", path)) {
                    // возврат всех задач
                    handleGetRequestAllTasks(httpExchange);
                } else if (Pattern.matches("^/epics/\\d+/subtask$", path)) {
                    String pathId = path.replaceAll("^/epics/(\\d+)/subtask$", "$1");
                    int id = parsePathId(pathId);
                    if (id == -1) {
                        baseHttpHandler.sendNotFound(httpExchange); //404
                    } else {
                        handleGetRequestIdEpicSubTasks(httpExchange, id);
                    }
                } else {
                    // возврат по айди
                    String pathId = path.replaceFirst("/epics/","");
                    int id = parsePathId(pathId);
                    if (id == -1) {
                        baseHttpHandler.sendNotFound(httpExchange); //404
                    } else {
                        handleGetRequestIdTask(httpExchange, id);
                    }
                }
                break;
            case "DELETE":
                if (Pattern.matches("^/epics$", path)) {
                    handleDeleteRequestTasks(httpExchange);
                } else {
                    String pathId = path.replaceFirst("/epics/","");
                    int id = parsePathId(pathId);
                    if (id == -1) {
                        baseHttpHandler.sendNotFound(httpExchange); //404
                    } else {
                        handleDeleteRequestIdTask(httpExchange, id);
                    }
                }
                break;
            default:
                baseHttpHandler.sendNotFound(httpExchange); //404
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void handleGetRequestAllTasks(HttpExchange httpExchange) throws IOException {
        // обработайте GET-запрос в соответствии с условиями задания (сериализация)
        var result = gson.toJson(inMemoryTaskManager.getAddEpic());
        baseHttpHandler.sendText(httpExchange,result,200);
    }

    private static void handleGetRequestIdTask(HttpExchange httpExchange, int id) throws IOException {
        var result = inMemoryTaskManager.getIdEpic(id);
        if (result == null) {
            baseHttpHandler.sendNotFound(httpExchange);
        }
        baseHttpHandler.sendText(httpExchange,gson.toJson(result),200);
    }

    private static void handleGetRequestIdEpicSubTasks(HttpExchange httpExchange, int id) throws IOException {
        var result = inMemoryTaskManager.getListSubTaskEpic(id);
        if (result == null) {
            baseHttpHandler.sendNotFound(httpExchange);
        }
        baseHttpHandler.sendText(httpExchange,gson.toJson(result),200);
    }

    private static void handlePostRequest(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic taskDeserialized = gson.fromJson(body, Epic.class);
        if (taskDeserialized.getId() == null) {
            inMemoryTaskManager.addEpic(taskDeserialized);
        } else {
            inMemoryTaskManager.updatingEpicTask(taskDeserialized.getId(), taskDeserialized);
        }
        int responseCode = inMemoryTaskManager.getCode();
        if (responseCode == 201) {
            baseHttpHandler.sendText(httpExchange,"Успешно",responseCode);
        } else {
            baseHttpHandler.sendHasInteractions(httpExchange);
        }
        inMemoryTaskManager.setCode(0); // обнулил код в менеджере
    }

    private static void handleDeleteRequestTasks(HttpExchange httpExchange) throws IOException {
        baseHttpHandler.sendText(httpExchange,"Успешно",200);
        inMemoryTaskManager.deleteAllEpic();
    }

    private static void handleDeleteRequestIdTask(HttpExchange httpExchange, int id) throws IOException {
        baseHttpHandler.sendText(httpExchange,"Успешно",200);
        inMemoryTaskManager.deleteIdEpic(id);
    }
}

