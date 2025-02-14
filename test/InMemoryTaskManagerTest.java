import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.TypeOfTask.*;

public class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManagerTest;
    Task task0;
    Task task1;
    Task task;
    Task task2;
    Task task3;
    Epic epic4;
    SubTask subTask5;
    SubTask subTask6;
    SubTask subTask7;
    SubTask subTask8;

    @BeforeEach
    void setUpTask() {
        inMemoryTaskManagerTest = new InMemoryTaskManager();
        task0 = new Task("Task", "Task пустышка", NEW);
        task1 = new Task("TaskTime", "Первая 11:00 - 11:10", NEW, "2021-12-20T11:00:00", 10L);
        task2 = new Task("TaskTime", "Вторая середина 11:15 - 11:25", NEW, "2021-12-20T11:15:00", 10L);
        task3 = new Task("TaskTime", "Третья 11:25 - 11:35", NEW, "2021-12-20T11:25:00", 10L);
        task = new Task("TaskTime", "Не валидная 11:00 - 11:05", NEW, "2021-12-20T11:05:00", 10L);

        inMemoryTaskManagerTest.addTask(task0);
        inMemoryTaskManagerTest.addTask(task1);
        inMemoryTaskManagerTest.addTask(task2);
        inMemoryTaskManagerTest.addTask(task3);
        inMemoryTaskManagerTest.addTask(task);

        Epic epic4 = new Epic("Epic", "Epic description", NEW);
        SubTask subTask5 = new SubTask("SubTask", "Пустышка", NEW);
        SubTask subTask6 = new SubTask("SubTask", "Первая 18:00", NEW, "2021-12-20T18:00:00", 10L);
        SubTask subTask7 = new SubTask("SubTask", "Вторая 18:10", NEW, "2021-12-20T18:10:00", 10L);
        SubTask subTask8 = new SubTask("SubTask", "Третья 18:20 ", NEW, "2021-12-20T18:20:00", 10L);
        inMemoryTaskManagerTest.addEpic(epic4);
        inMemoryTaskManagerTest.addSubTask(4, subTask5);
        inMemoryTaskManagerTest.addSubTask(4, subTask6);
        inMemoryTaskManagerTest.addSubTask(4, subTask7);
        inMemoryTaskManagerTest.addSubTask(4, subTask8);
    }

    @Test
    void deleteIssues() {
        inMemoryTaskManagerTest.deleteIdTask(1);
        inMemoryTaskManagerTest.deleteIdTask(3);
        final List<Task> tasks = inMemoryTaskManagerTest.getAddTask();
        final List<Task> tasksTime = inMemoryTaskManagerTest.getPrioritizedTasks();
        assertEquals(2, tasks.size(), "Неверное количество всех задач.");
        assertEquals(4, tasksTime.size(), "Неверное количество приоритет задач.");
        assertEquals(task0, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task2, tasksTime.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewTask() {
        final List<Task> tasks = inMemoryTaskManagerTest.getAddTask();
        final List<Task> tasksTime = inMemoryTaskManagerTest.getPrioritizedTasks();
        assertEquals(4, tasks.size(), "Неверное количество всех задач.");
        assertEquals(6, tasksTime.size(), "Неверное количество приоритет задач.");
        assertEquals(task1, tasksTime.get(0), "Задачи не совпадают.");
        assertEquals(task3, tasksTime.get(2), "Задачи не совпадают.");
        // проверим, что пустышка не попала в задачи приоритета
        final boolean savedTask = tasksTime.contains(task0);
        Assertions.assertFalse(savedTask, "Задача найдена.");
    }

    @Test
    void UpdateTasks() {
        Task taskNotUpdating = new Task("TaskTime", "П11:05 - 11:15", DONE, "2021-12-20T11:05:00", 10L);
        Task taskUpdating = new Task("TaskTime", "П10:0 - 10:10", DONE, "2021-12-20T10:00:00", 10L);
        Task taskEmpty = new Task("TaskTime", "Пустышка", NEW);
        // обновиться не должна (не влезает в лимит)
        inMemoryTaskManagerTest.updatingTask(0, taskNotUpdating);
        // меняем статус и время (произойдет ротация, была последней, станет первой)
        inMemoryTaskManagerTest.updatingTask(3,taskUpdating);
        List<Task> tasks = inMemoryTaskManagerTest.getAddTask();
        List<Task> tasksTime = inMemoryTaskManagerTest.getPrioritizedTasks();
        assertEquals(task0, tasks.get(0), "Задача изменилась.");
        // убедимся, что поменялась
        boolean savedTask = tasksTime.contains(task3);
        Assertions.assertFalse(savedTask, "Задача найдена.");
        assertEquals(inMemoryTaskManagerTest.getIdTask(3), taskUpdating, "Задача не изменилась.");
        // убедимся, что произошла ротация
        assertEquals(taskUpdating, tasksTime.get(0), "Ротация не соблюдается.");
        // меняем на пустышку (стирание из tasksTime, изменение в tasks)
        inMemoryTaskManagerTest.updatingTask(3,taskEmpty);
        tasks = inMemoryTaskManagerTest.getAddTask();
        tasksTime = inMemoryTaskManagerTest.getPrioritizedTasks();
        savedTask = tasksTime.contains(taskUpdating);
        Assertions.assertFalse(savedTask, "Задача найдена.");
        savedTask = tasks.contains(taskEmpty);
    }

    @Test
    void addNewEpicSubTask() {
        final List<SubTask> tasks = inMemoryTaskManagerTest.getListSubTaskEpic(4);
        final List<Task> tasksTime = inMemoryTaskManagerTest.getPrioritizedTasks();
        //boolean savedTask = tasksTime.contains(task3);
        assertEquals(4, tasks.size(), "Неверное количество всех задач.");
        assertEquals(6, tasksTime.size(), "Неверное количество приоритет задач.");
        // проверим приоритет
        final SubTask subTask = inMemoryTaskManagerTest.getIdSubTask(8);
        assertEquals(subTask, tasksTime.get(5), "Неверный приоритет задач.");
        // проверим время выполнения Эпика
        final Epic epic = inMemoryTaskManagerTest.getIdEpic(4);
        assertEquals(epic.getTimeStart(), LocalDateTime.parse("2021-12-20T18:00:00"), "Время старта не совпадает.");
        assertEquals(epic.getTimeEnd(), LocalDateTime.parse("2021-12-20T18:30:00"), "Время финиша не совпадает.");
        // убедимся, что Эпик знает свои подзадачи
        final List<SubTask> getListSubTaskEpic = inMemoryTaskManagerTest.getListSubTaskEpic(4);
        assertEquals(4, getListSubTaskEpic.size(), "Неверное количество подзадач.");
    }
    @Test
    void deleteIssuesEpicSubTask() {
        inMemoryTaskManagerTest.deleteIdSubTask(6);
        inMemoryTaskManagerTest.deleteIdSubTask(8);
        final List<Task> tasksTime = inMemoryTaskManagerTest.getPrioritizedTasks();
        assertEquals(4, tasksTime.size(), "Неверное количество всех задач.");
        final List<SubTask> getListSubTaskEpic = inMemoryTaskManagerTest.getListSubTaskEpic(4);
        assertEquals(2, getListSubTaskEpic.size(), "Неверное количество всех задач.");
        // проверим, что время обновилось
        final Epic epic = inMemoryTaskManagerTest.getIdEpic(4);
        assertEquals(epic.getTimeStart(), LocalDateTime.parse("2021-12-20T18:10:00"), "Время старта не совпадает.");
        assertEquals(epic.getTimeEnd(), LocalDateTime.parse("2021-12-20T18:20:00"), "Время финиша не совпадает.");
        // проверим, что что приоритет всех задач тоже поменялся
        final SubTask subTask = inMemoryTaskManagerTest.getIdSubTask(7);
        assertEquals(subTask, tasksTime.get(3), "Неверный приоритет задач.");
    }

    @Test
    void UpdateEpicSubTasks() {
        SubTask taskNotUpdating = new SubTask("SubTaskTime", "П18:05 - 18:15", DONE, "2021-12-20T18:05:00", 10L);
        SubTask taskUpdating = new SubTask("SubTaskTime", "П09:0 - 09:10", DONE, "2021-12-20T09:00:00", 10L);
        SubTask taskEmpty = new SubTask("SubTaskTime", "Пустышка", NEW);
        // обновиться не должна (не влезает в лимит)
        inMemoryTaskManagerTest.updatingSubTask(8, taskNotUpdating);
        // меняем статус и время (произойдет ротация, была последней, станет первой)
        inMemoryTaskManagerTest.updatingSubTask(8,taskUpdating);
        List<SubTask> tasks = inMemoryTaskManagerTest.getAddSubTask();
        List<Task> tasksTime = inMemoryTaskManagerTest.getPrioritizedTasks();
        final SubTask subTask = inMemoryTaskManagerTest.getIdSubTask(8);
        assertEquals(subTask, tasksTime.get(0), "Приоритет не соблюдается.");
        // проверим, что время обновилось
        final Epic epic = inMemoryTaskManagerTest.getIdEpic(4);
        assertEquals(epic.getTimeStart(), LocalDateTime.parse("2021-12-20T09:00:00"), "Время старта не совпадает.");
        assertEquals(epic.getTimeEnd(), LocalDateTime.parse("2021-12-20T18:20:00"), "Время финиша не совпадает.");
        // меняем на пустышку
        inMemoryTaskManagerTest.updatingSubTask(8, taskEmpty);
        // убедились, что она сидит в List<SubTask> и больше не сидит в List<Task>
        boolean savedTasksTime = tasks.contains(taskEmpty);
        boolean savedTasks = tasksTime.contains(subTask);
        Assertions.assertTrue(savedTasks, "Задача найдена.");
        Assertions.assertFalse(savedTasksTime, "Задача найдена.");
    }
}