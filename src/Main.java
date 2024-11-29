

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("------------------Добавление Epic--------------------");
        Epic epic = new Epic("План на сегодня","Кухня и комната",TypeOfTask.NEW);
        Epic epic2 = new Epic("План на завтра","Яндекс",TypeOfTask.NEW);
        taskManager.addEpic(epic);
        taskManager.addEpic(epic2);
        System.out.println("------------------Вывод Epic--------------------");
        System.out.println(taskManager.getAddEpic());
        System.out.println("------------------Добавление SubTask--------------------");
        SubTask subtask1 = new SubTask("утро", "убраться на кухне",TypeOfTask.DONE);
        SubTask subtask2 = new SubTask("вечер", "убраться в комнате",TypeOfTask.DONE);
        SubTask subtask3 = new SubTask("день","Позаниматься кодом ",TypeOfTask.NEW);
        taskManager.addSubTask(0,subtask1);
        taskManager.addSubTask(0,subtask2);
        taskManager.addSubTask(1,subtask3);

        System.out.println("------------------вывод подзадач эпика по айди--------------------");
        System.out.println(taskManager.getListSubTaskEpic(0));
        System.out.println(taskManager.getListSubTaskEpic(1));

        System.out.println("------------------вывод всех подзадач--------------------");
        System.out.println(taskManager.getAddSubTask());
        System.out.println("------------------удаление подзадач эпика--------------------");
//        taskManager.deleteAllSubTask();
//        System.out.println(taskManager.getListSubTaskEpic(0));
//        System.out.println(taskManager.getListSubTaskEpic(1));
//        System.out.println(taskManager.getAddEpic());
        System.out.println("---------------------Получение по идентификатору.----------------------------");
        System.out.println(taskManager.getIdSubTask(4));
        System.out.println(taskManager.getIdEpic(10));
        //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
        System.out.println("---------------------Обновление.----------------------------");
        System.out.println(taskManager.getAddSubTask());
        taskManager.updatingSubTask(2,new SubTask("утро", "убраться на кухне",TypeOfTask.NEW));
        System.out.println(taskManager.getAddSubTask());
//        taskManager.updatingEpicTask(1,new Epic("План был хорош","не фартануло",TypeOfTask.NEW));
//        System.out.println(taskManager.getAddEpic());
        System.out.println("---------------------Удаление по айди.----------------------------");
        System.out.println(taskManager.getAddSubTask());
        taskManager.deleteIdSubTask(1);
        System.out.println(taskManager.getAddSubTask());




    }
}

