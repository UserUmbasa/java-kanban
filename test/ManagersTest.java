import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.TypeOfTask;
import tracker.service.Managers;
import tracker.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    static TaskManager taskManager = Managers.getDefault();

    @BeforeAll
    public static void setUp() {
        taskManager.addTask(new Task("Task", "--0--", TypeOfTask.NEW));
        taskManager.addEpic(new Epic("Epic", "--1--", TypeOfTask.NEW));
        taskManager.addSubTask(1, new SubTask("SubTask", "--2--", TypeOfTask.NEW));
    }

    //check the history
    @Test
    public void checkTaskHistoryNull() {
        assertNotNull(taskManager.getIdTask(0), "Cписок Task пуст.");
        assertNotNull(taskManager.getIdEpic(1), "Cписок Epic пуст.");
        assertNotNull(taskManager.getIdSubTask(2), "Cписок SubTask пуст.");
        //сразу проверю историю (согласно ТЗ при вызове этих методов)
        Assertions.assertEquals(3, taskManager.getHistory().size(), "Cписок истории пуст.");
    }
}
