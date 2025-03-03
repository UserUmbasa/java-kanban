package tracker.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

public class DurationTypeAdapter extends TypeAdapter<Duration> {

    // ISO 8601 формат для Duration (например, PT1H30M15S)
    @Override
    public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue(); // Обработка null значений
        } else {
            jsonWriter.value(duration.toString()); // Запись Duration в виде ISO 8601 строки
        }
    }

    @Override
    public Duration read(final JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == com.google.gson.stream.JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        try {
            String durationString = jsonReader.nextString();
            return Duration.parse(durationString);
        } catch (DateTimeParseException e) {
            throw new IOException(e.getMessage(), e);
        }
    }
}
