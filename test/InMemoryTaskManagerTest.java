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
/**
 * Нужно ли проверять все методы не знаю, т.к как в прошлом спринте проверялось все неоднократно.
 * Жду Вашей реакции.
 */

public class InMemoryTaskManagerTest {

    InMemoryTaskManager inMemoryTaskManagerTest = new InMemoryTaskManager();

    @Test
    void addNewTask(){
        Task task = new Task("TestTask", "Task description", NEW);
        inMemoryTaskManagerTest.addTask(task);
        final Task savedTask = inMemoryTaskManagerTest.getIdTask(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = inMemoryTaskManagerTest.getAddTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        /**
         * Далее в тестах сравнивать эпики и субтаски не вижу смысла, т.к. переопределение методов в наследниках
         * public final boolean equals(Object o) и  public final int hashCode() запрещены и соответственно
         *  assertEquals(task, savedTask, "Задачи не совпадают.") будет работать аналогично
         */
    }
    @Test
    void addNewEpicSubTask(){
        Epic epic = new Epic("Epic", "Epic description", NEW);
        SubTask subTask = new SubTask("SubTask", "SubTask description", NEW);
        inMemoryTaskManagerTest.addEpic(epic);
        final Epic savedEpic = inMemoryTaskManagerTest.getIdEpic(epic.getId());
        assertNotNull(savedEpic, "Задача не найдена.");
        inMemoryTaskManagerTest.addSubTask(epic.getId(), subTask);
        final var result = inMemoryTaskManagerTest.getAddSubTask();
        final boolean savedSubTask = result.contains(subTask);
        Assertions.assertTrue(savedSubTask,"Задача не найдена.");

    }

}
