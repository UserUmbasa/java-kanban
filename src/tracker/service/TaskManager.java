package tracker.service;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;

import java.util.List;

public interface TaskManager  {
    void addTask(Task task);

    void addSubTask(int idEpic, SubTask subtask);

    void addEpic(Epic epic);

    //------------------- Получение списка всех задач.------------------
    List<Task> getAddTask();

    List<SubTask> getAddSubTask();

    List<Epic> getAddEpic();

    //----------------------------Удаление всех задач.---------------------------
    void deleteAllTask();

    void deleteAllSubTask();

    void deleteAllEpic();

    //---------------------Получение по идентификатору.----------------------------
    Task getIdTask(Integer id);

    SubTask getIdSubTask(Integer id);

    Epic getIdEpic(Integer id);

    //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
    void updatingTask(Integer id, Task task);

    void updatingSubTask(Integer id, SubTask subTask);

    void updatingEpicTask(Integer id, Epic epic);

    //-----------------------Удаление по идентификатору--------------------------
    void deleteIdTask(Integer id);

    void deleteIdSubTask(Integer id);

    void deleteIdEpic(Integer id);

    //------------------------- Получение списка всех подзадач определённого эпика-------------
    List<SubTask> getListSubTaskEpic(Integer id);
    //-------------------------- Получение истории просмотров ------------------
    List<Task> getHistory();
}
