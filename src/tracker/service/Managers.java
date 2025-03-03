package tracker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tracker.model.DurationTypeAdapter;
import tracker.model.LocalTimeTypeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getDefaultGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .create();
        return gsonBuilder.create();
    }
}
