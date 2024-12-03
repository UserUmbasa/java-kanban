package tracker;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.service.TaskManager;
import tracker.model.TypeOfTask;

public class Main {
    public static void main(String[] args) {
        CheckingSimpleTasks();
        CheckingEpicTasks();
    }

    public static void CheckingSimpleTasks() {
        TaskManager taskManager = new TaskManager();
        System.out.println("------------------Добавление Task--------------------");
        taskManager.addTask(new Task("Cтол", "Убрать", TypeOfTask.NEW));
        taskManager.addTask(new Task("Пол", "помыть", TypeOfTask.NEW));
        System.out.println(taskManager.getAddTask());
        System.out.println("------------------Обновление Task--------------------");
        taskManager.updatingTask(0, new Task("Cтол", "попросить жену", TypeOfTask.DONE));
        taskManager.updatingTask(1, new Task("Пол", "забить болт", TypeOfTask.IN_PROGRESS));
        System.out.println(taskManager.getAddTask());
        System.out.println("------------------Возврат Task по айди --------------------");
        System.out.println(taskManager.getIdTask(6));
        System.out.println("------------------Удаление Task по айди --------------------");
        taskManager.deleteIdTask(6);
        System.out.println(taskManager.getAddTask());
        System.out.println("------------------Удаление Tasks --------------------");
        taskManager.deleteAllTask();
        System.out.println(taskManager.getAddTask());
    }

    public static void CheckingEpicTasks() {
        TaskManager taskManager = new TaskManager();
        System.out.println("------------------Добавление Epics--------------------");
        Epic epic = new Epic("План на сегодня", "Кухня и комната", TypeOfTask.NEW); //0
        Epic epic2 = new Epic("План на завтра", "Яндекс", TypeOfTask.NEW); //1
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        System.out.println("------------------Вывод Epics--------------------");
        System.out.println(taskManager.getAddEpic());
        System.out.println();
        System.out.println("------------------Добавление SubTasks--------------------");
        SubTask subtask1 = new SubTask("утро", "убраться на кухне", TypeOfTask.DONE);//2
        SubTask subtask2 = new SubTask("вечер", "убраться в комнате", TypeOfTask.DONE);//3
        SubTask subtask3 = new SubTask("день", "Позаниматься кодом ", TypeOfTask.NEW);//4
        SubTask subtask4 = new SubTask("день", "Позаниматься кодом ", TypeOfTask.NEW);//4
        taskManager.addSubTask(0, subtask1);
        taskManager.addSubTask(0, subtask2);
        taskManager.addSubTask(0, subtask3);
        taskManager.addSubTask(1, subtask4); //вернет тот же объект
        System.out.println("------------------Вывод SubTask--------------------");
        System.out.println(taskManager.getAddSubTask());
        System.out.println();
        System.out.println("------------------Вывод SubTask [id]--------------------");
        System.out.println(taskManager.getIdSubTask(5));
        System.out.println(taskManager.getIdSubTask(3));
        System.out.println("------------------Вывод EpicTask [id]--------------------");
        System.out.println(taskManager.getIdEpic(5));
        System.out.println(taskManager.getIdEpic(0));
        System.out.println("------------------Вывод SubTask -> Epic [id]--------------------");
        System.out.println(taskManager.getListSubTaskEpic(0));
        System.out.println(taskManager.getListSubTaskEpic(1));
        System.out.println(taskManager.getListSubTaskEpic(8));
        System.out.println("------------------обновление SubTask [id]--------------------");
        System.out.println(taskManager.getAddEpic());
        System.out.println(taskManager.getAddSubTask());
        System.out.println("------------------обновление......--------------------");
        taskManager.updatingSubTask(2,new SubTask("утро", "убраться на кухне",TypeOfTask.IN_PROGRESS));
        System.out.println(taskManager.getAddEpic());
        System.out.println(taskManager.getAddSubTask());
//        System.out.println("------------------обновление Epic  [id]--------------------");
//        taskManager.updatingEpicTask(0,new tracker.model.Epic("План на сегодня","Кухня и комната",tracker.model.TypeOfTask.NEW));
//        System.out.println(taskManager.getAddSubTask());
//        System.out.println(taskManager.getAddEpic());
//        System.out.println("------------------Удаление AllSubTask --------------------");
//        taskManager.deleteAllSubTask();
//        System.out.println(taskManager.getAddSubTask());
//        System.out.println(taskManager.getAddEpic());
        System.out.println("------------------Удаление AllEpic --------------------");
        taskManager.deleteAllEpic();
        System.out.println(taskManager.getAddSubTask());
        System.out.println(taskManager.getAddEpic());

//        System.out.println(taskManager.getListSubTaskEpic(0));
//        System.out.println(taskManager.getListSubTaskEpic(1));
//        System.out.println(taskManager.getAddEpic());
    }

}

