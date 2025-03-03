package tracker.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import tracker.server.handlers.*;
import tracker.service.InMemoryTaskManager;
import tracker.service.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private final HttpServer server;
    private static Gson gson;
    public static InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();

    public HttpTaskServer() throws IOException {
        gson = Managers.getDefaultGson();
        server = HttpServer.create(new InetSocketAddress("localhost",PORT), 0);
        server.createContext("/tasks", new HandlerTasks());
        server.createContext("/subtasks", new HandlerSubtasks());
        server.createContext("/epics", new HandlerEpics());
        server.createContext("/history", new HandlerHistory());
        server.createContext("/prioritized", new HandlerPrioritized());
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    public static Gson getGson() {
        return gson;
    }

    public static InMemoryTaskManager getInMemoryTaskManager() {
        return inMemoryTaskManager;
    }

    public void start() {
        System.out.println("Starting HttpTaskServer: " + PORT);
        server.start();
    }

    public void stop() {
        System.out.println("Stop HttpTaskServer: " + PORT);
        server.stop(0);
    }
}
