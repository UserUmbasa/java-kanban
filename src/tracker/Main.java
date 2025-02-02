package tracker;
import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.FileBackedTaskManager;
import java.io.File;
import static tracker.model.TypeOfTask.NEW;

public class Main {

    public static void main(String[] args) {
        //пусть будет, не уверен в правильности происходящего
        File file = FileBackedTaskManager.fileBackedTaskManager();
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        Task task0 = new Task("task 0", "описание 0", NEW);
        Task task1 = new Task("task 1", "описание 1", NEW);
        Epic task2 = new Epic("Epic 2", "описание 2", NEW);
        SubTask subTask3 = new SubTask("subTask 3", "описание 3", NEW);
        SubTask subTask4 = new SubTask("subTask 4", "описание 4", NEW);
        SubTask subTask5 = new SubTask("subTask 5", "описание 5", NEW);
        fileBackedTaskManager.addTask(task0);
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addEpic(task2);
        fileBackedTaskManager.addSubTask(2,subTask3);
        fileBackedTaskManager.addSubTask(2,subTask4);
        fileBackedTaskManager.addSubTask(2,subTask5);
        System.out.println(fileBackedTaskManager.getListSubTaskEpic(2));
        //fileBackedTaskManager.deleteIdTask(0);
        fileBackedTaskManager.deleteIdSubTask(4);
        System.out.println(fileBackedTaskManager);
        //System.out.println(fileBackedTaskManager.getAddEpic());
        //System.out.println(fileBackedTaskManager.getAddSubTask());
    }
}

