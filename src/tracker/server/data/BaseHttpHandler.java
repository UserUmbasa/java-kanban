package tracker.server.data;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {

    public void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void sendNotFound(HttpExchange h) throws IOException {
        byte[] resp = "Такого объекта нет".getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(404,resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    public void sendHasInteractions(HttpExchange h) throws IOException {
        byte[] resp = "Объекты пересекаются".getBytes(StandardCharsets.UTF_8);
        h.sendResponseHeaders(406,resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
