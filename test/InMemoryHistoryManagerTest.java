import org.junit.jupiter.api.Test;
import tracker.model.*;
import tracker.service.HistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    HistoryManager historyManager = new InMemoryHistoryManager();
    @Test
    void checkAddingDifferentTypeTasks() {
        historyManager.add(new Task("Task", "--1--", TypeOfTask.NEW));
        historyManager.add(new SubTask("SubTask", "--2--", TypeOfTask.NEW));
        historyManager.add(new Epic("Epic", "--3--", TypeOfTask.NEW));
        final List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История пустая.");
    }
    @Test
    void checkOverflowQueue() {
        for (int i = 0; i < 20; i++) {
            Task task = new Task("Task", "----", TypeOfTask.NEW);
            task.setId(i);
            historyManager.add(task);
        }
        final List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "Массив переполнен.");
        assertEquals(10, history.getFirst().getId(), "Очередь не соблюдается");
        assertEquals(19, history.getLast().getId(), "Очередь не соблюдается");
    }

}
