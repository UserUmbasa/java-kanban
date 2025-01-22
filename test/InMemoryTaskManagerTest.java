import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.InMemoryTaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tracker.model.TypeOfTask.*;

public class InMemoryTaskManagerTest {

    InMemoryTaskManager inMemoryTaskManagerTest = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        Task task = new Task("TestTask", "Task description", NEW);
        inMemoryTaskManagerTest.addTask(task);
        final Task savedTask = inMemoryTaskManagerTest.getIdTask(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = inMemoryTaskManagerTest.getAddTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpicSubTask() {
        Epic epic = new Epic("Epic", "Epic description", NEW);
        SubTask subTask = new SubTask("SubTask", "SubTask description", NEW);
        inMemoryTaskManagerTest.addEpic(epic);
        final Epic savedEpic = inMemoryTaskManagerTest.getIdEpic(epic.getId());
        assertNotNull(savedEpic, "Задача не найдена.");
        inMemoryTaskManagerTest.addSubTask(epic.getId(), subTask);
        final var result = inMemoryTaskManagerTest.getAddSubTask();
        final boolean savedSubTask = result.contains(subTask);
        Assertions.assertTrue(savedSubTask, "Задача не найдена.");

    }

    @Test
    void checkHistoryCallQueue() {
        Task task = new Task("Task", "0", NEW);
        Epic epic = new Epic("Epic", "1", NEW);
        SubTask subTask = new SubTask("SubTask", "2", NEW);
        inMemoryTaskManagerTest.addTask(task);
        inMemoryTaskManagerTest.addEpic(epic);
        inMemoryTaskManagerTest.addSubTask(epic.getId(), subTask);
        //очередь 0->1->2->0->2 вывод 1->0->2
        inMemoryTaskManagerTest.getIdTask(task.getId()); //0
        inMemoryTaskManagerTest.getIdEpic(epic.getId()); //1
        inMemoryTaskManagerTest.getIdSubTask(subTask.getId()); //2
        inMemoryTaskManagerTest.getIdTask(task.getId());
        inMemoryTaskManagerTest.getIdSubTask(subTask.getId()); //2
        List<Task> history = inMemoryTaskManagerTest.getHistory();
        System.out.println(history);
        assertEquals(1, history.getFirst().getId(), "Очередь не соблюдается");
        assertEquals(2, history.getLast().getId(), "Очередь не соблюдается");
    }

    @Test
    void checkHistoryDeletionID() {
        Task task = new Task("Task", "0", NEW);
        Epic epic = new Epic("Epic", "1", NEW);
        SubTask subTask = new SubTask("SubTask", "2", NEW);
        inMemoryTaskManagerTest.addTask(task);
        inMemoryTaskManagerTest.addEpic(epic);
        inMemoryTaskManagerTest.addSubTask(epic.getId(), subTask);
        //очередь 0->1->2, удаляем таск и субтаск, размер и эпик - 1
        inMemoryTaskManagerTest.getIdTask(task.getId()); //0
        inMemoryTaskManagerTest.getIdEpic(epic.getId()); //1
        inMemoryTaskManagerTest.getIdSubTask(subTask.getId()); //2
        inMemoryTaskManagerTest.deleteIdTask(task.getId());
        inMemoryTaskManagerTest.deleteIdSubTask(subTask.getId());
        List<Task> history = inMemoryTaskManagerTest.getHistory();
        assertEquals(1, history.size(), "Неверный размер массива истории");
        assertEquals(1, history.getFirst().getId(), "Очередь не соблюдается");
        assertEquals(1, history.getLast().getId(), "Очередь не соблюдается");
    }

    @Test
    void checkHistoryDeletionAllTasks() {
        Task task = new Task("Task", "0", NEW);
        Epic epic = new Epic("Epic", "1", NEW);
        SubTask subTask = new SubTask("SubTask", "2", NEW);
        inMemoryTaskManagerTest.addTask(task);
        inMemoryTaskManagerTest.addEpic(epic);
        inMemoryTaskManagerTest.addSubTask(epic.getId(), subTask);
        //очередь 0->1->2, удаляем таск и субтаск, размер и эпик - 1
        inMemoryTaskManagerTest.getIdTask(task.getId()); //0
        inMemoryTaskManagerTest.getIdEpic(epic.getId()); //1
        inMemoryTaskManagerTest.getIdSubTask(subTask.getId()); //2
        inMemoryTaskManagerTest.deleteIdTask(task.getId());
        inMemoryTaskManagerTest.deleteIdSubTask(subTask.getId());
        List<Task> history = inMemoryTaskManagerTest.getHistory();
        assertEquals(1, history.size(), "Неверный размер массива истории");
        assertEquals(1, history.getFirst().getId(), "Очередь не соблюдается");
        assertEquals(1, history.getLast().getId(), "Очередь не соблюдается");
    }
}
