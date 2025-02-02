import org.junit.jupiter.api.Test;
import tracker.model.Task;
import tracker.service.FileBackedTaskManager;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.TypeOfTask.NEW;

public class FileBackedTaskManagerTest {
    @Test
    void checkTaskLoading() throws IOException {
        //создал временный файл (пустой)
        File tempFile = File.createTempFile("test", ".txt");
        //загрузился с пустого
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = fileBackedTaskManager.getAddTask();
        //чек на пустое
        assertEquals(0, tasks.size(), "Неверное количество задач.");
        //добавили
        Task task0 = new Task("task 0", "описание 0", NEW);
        Task task1 = new Task("task 1", "описание 1", NEW);
        fileBackedTaskManager.addTask(task0);
        fileBackedTaskManager.addTask(task1);
        tasks = fileBackedTaskManager.getAddTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        // создал второй объект (загружусь с него)
        FileBackedTaskManager fileBackedTaskManager2 = FileBackedTaskManager.loadFromFile(tempFile);
        //сравню два объекта по задачам
        List<Task> tasks2 = fileBackedTaskManager2.getAddTask();
        assertEquals(tasks, tasks2, "Файлы не совпадают.");
        tempFile.deleteOnExit();
    }
}