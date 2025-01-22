import org.junit.jupiter.api.Test;
import tracker.model.*;
import tracker.service.HistoryManager;
import tracker.service.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void checkAddingDifferentTypeTasks() {
        Task task = new Task("Task", "--0--", TypeOfTask.NEW);
        task.setId(0);
        historyManager.add(task);
        SubTask subTask = new SubTask("SubTask", "--1--", TypeOfTask.NEW);
        subTask.setId(1);
        historyManager.add(subTask);
        Epic epic = new Epic("Epic", "--2--", TypeOfTask.NEW);
        epic.setId(2);
        historyManager.add(epic);
        final List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История пустая.");
    }

    @Test
    void checkOrderFilling() {
        for (int i = 0; i < 10; i++) {
            Task task = new Task("Task", "----", TypeOfTask.NEW);
            task.setId(i);
            historyManager.add(task);
        }
        final List<Task> history = historyManager.getHistory();
        assertEquals(0, history.getFirst().getId(), "Очередь не соблюдается");
        assertEquals(9, history.getLast().getId(), "Очередь не соблюдается");
    }

    @Test
    void checkIssueDeletedHistory() {
        for (int i = 0; i < 5; i++) {
            Task task = new Task("Task", "----", TypeOfTask.NEW);
            task.setId(i);
            historyManager.add(task);
        }
        historyManager.remove(0);
        historyManager.remove(2);
        final List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "Задачи не удалены");
    }
}
