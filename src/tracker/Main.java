package tracker;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.InMemoryTaskManager;
import tracker.service.Managers;
import tracker.service.TaskManager;
import tracker.model.TypeOfTask;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        taskManager.addTask(new Task("Task", "--0--", TypeOfTask.NEW));
        taskManager.addEpic(new Epic("Epic", "--1--", TypeOfTask.NEW));;
        taskManager.addSubTask(1,new SubTask("SubTask", "--2--", TypeOfTask.NEW));
    }
}

