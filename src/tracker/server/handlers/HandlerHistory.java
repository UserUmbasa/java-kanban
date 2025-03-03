package tracker.server.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.server.HttpTaskServer;
import tracker.server.data.BaseHttpHandler;
import tracker.service.Managers;

import java.io.IOException;

public class HandlerHistory implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // метод из запроса
        String method = exchange.getRequestMethod();
        if (method.equals("GET")) {
            Gson gson = Managers.getDefaultGson();
            BaseHttpHandler baseHttpHandler = new BaseHttpHandler();
            var history = HttpTaskServer.inMemoryTaskManager.getHistory();
            var result = gson.toJson(history);
            baseHttpHandler.sendText(exchange,result,200);
        }
    }
}
