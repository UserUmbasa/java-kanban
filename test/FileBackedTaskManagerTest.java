import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.FileBackedTaskManager;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.TypeOfTask.NEW;

public class FileBackedTaskManagerTest {
    // проверка на загрузку простых задач
    File tempFileTask = new File("test\\testTask.txt");

    // проверка на загрузку всех задач
    File tempFileAllTask = new File("test\\testAllTasks.txt");

    // проверка на сравнение
    File tempFileMatch = new File("test\\testMatch.txt");

    FileBackedTaskManager fileBackedTaskManager ;

    @Test
    void checkTaskLoading() {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFileTask);
        final List<Task> tasks = fileBackedTaskManager.getAddTask();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void checkAllTaskLoading() {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFileAllTask);
        final List<SubTask> subTasks = fileBackedTaskManager.getAddSubTask();
        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(3, subTasks.size(), "Неверное количество задач.");
        final List<Epic> epic = fileBackedTaskManager.getAddEpic();
        assertNotNull(epic, "Задачи не возвращаются.");
        assertEquals(1, epic.size(), "Неверное количество задач.");
    }

    @Test
    void checkFileMatch() throws IOException {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFileMatch);
        Task task0 = new Task("task 0", "описание 0", NEW);
        Task task1 = new Task("task 1", "описание 1", NEW);
        fileBackedTaskManager.addTask(task0);
        fileBackedTaskManager.addTask(task1);
        Path tempFileMatch = Paths.get("test\\testMatch.txt");
        Path tempFileTask = Paths.get("test\\testTask.txt");
        // Чтение строк из файлов и сохранение их в List
        List<String> expectedLines = readLines(tempFileMatch); // Список строк из первого файла
        List<String> actualLines = readLines(tempFileTask);  // Список строк из второго файла
        assertEquals(expectedLines, actualLines, "Файлы не совпадают.");
    }

    private List<String> readLines(Path filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}