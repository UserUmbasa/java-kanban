

public class Main {
    public static void main(String[] args) {
        TaskMeneger taskMeneger = new TaskMeneger();
        System.out.println("------------------- Получение списка всех задач.------------------");
        Task task = new Task("Стол", "прибраться на столе",TypeOfTask.NEW);
        Task task2 = new Task("Телевизор", "Протереть пыль",TypeOfTask.NEW);
        taskMeneger.addTask(task);
        taskMeneger.addTask(task2);
        System.out.println("---------------вывод простых задач-------------------");
        System.out.println(taskMeneger.getAddTask());
        System.out.println("------------------вывод подзадач--------------------");
        SubTask subtask1 = new SubTask("утро", "убраться на кухне",TypeOfTask.NEW);
        SubTask subtask2 = new SubTask("вечер", "убраться в комнате",TypeOfTask.NEW);
        SubTask subtask3 = new SubTask("день","Позаниматься кодом ",TypeOfTask.NEW);
        subtask2.addTask(task);
        subtask2.addTask(task2);
        taskMeneger.addSubTask(subtask1);
        taskMeneger.addSubTask(subtask2);
        taskMeneger.addSubTask(subtask3);
        System.out.println(taskMeneger.getAddSubTask());
        System.out.println("------------------вывод эпиков--------------------");
        Epic epic = new Epic("План на сегодня","Кухня и комната",TypeOfTask.NEW);
        Epic epic2 = new Epic("План на завтра","Яндекс",TypeOfTask.NEW);
        taskMeneger.addEpic(epic);
        taskMeneger.addEpic(epic2);
        System.out.println(taskMeneger.getAddEpic());
        System.out.println("------------------вывод подзадач эпика по айди--------------------");
        epic.setSubTask(45,subtask1);
        epic.setSubTask(60,subtask2);
        var SubTasklist = taskMeneger.getListSubTaskEpic(5);
        System.out.println(SubTasklist);
        System.out.println("------------------обновление статуса task--------------------");
        Task updatingTask = new Task("Стол", "прибраться на столе",TypeOfTask.IN_PROGRESS);
        taskMeneger.updatingTask(0,updatingTask);
        System.out.println(taskMeneger.getAddTask());
        System.out.println("------------------обновление статуса SubTask--------------------");
        SubTask updatingSubtask = new SubTask("Днем","Позаниматься кодом ",TypeOfTask.IN_PROGRESS);
        taskMeneger.updatingSubTask(4,updatingSubtask);
        System.out.println(taskMeneger.getAddSubTask());
        System.out.println("------------------обновление статуса epic--------------------");
        SubTask updatingSubtask1 = new SubTask("утром", "убраться на кухне",TypeOfTask.DONE);
        SubTask updatingSubtask2 =new SubTask("вечером", "убраться в комнате",TypeOfTask.IN_PROGRESS);
        epic.setSubTask(45,updatingSubtask1);
        epic.setSubTask(60,updatingSubtask2);
        System.out.println(taskMeneger.getAddEpic());
        System.out.println("------------------удаление по айди--------------------");
        System.out.println(taskMeneger.getAddTask());
        taskMeneger.deleteIdTask(0);
        System.out.println(taskMeneger.getAddTask());


    }
}

