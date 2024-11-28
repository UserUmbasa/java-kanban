import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskMeneger {
    private Integer id =0;
    private final Map<Integer,Task> mapTask;
    private final Map<Integer,Epic> mapEpic;
    private final Map<Integer,SubTask> mapSubTask;

    public TaskMeneger() {
        this.mapTask = new HashMap<>();
        this.mapEpic = new HashMap<>();
        this.mapSubTask = new HashMap<>();
    }
    //------------------- Получение списка всех задач.------------------
    public List<Task> getAddTask() {
        List<Task> tasks = new ArrayList<>(mapTask.values());
        return tasks;
    }
    public List<SubTask> getAddSubTask() {
        List<SubTask> subTasks = new ArrayList<>(mapSubTask.values());
        return subTasks;
    }
    public List<Epic> getAddEpic() {
        List<Epic> epicTasks = new ArrayList<>(mapEpic.values());
        return epicTasks;
    }
    //----------------------------Удаление всех задач.---------------------------
    public void deleteAllTask () {
        mapTask.clear();
    }
    public void deleteAllSubTask () {
        mapSubTask.clear();
    }
    public void deleteAllEpic () {
        mapEpic.clear();
    }
    //---------------------Получение по идентификатору.----------------------------
    public Task getIdTask(int id) {
        if(mapTask.containsKey(id)) {
            return mapTask.get(id);
        }
        return null;
    }
    public SubTask getIdSubTask(int id) {
        if(mapSubTask.containsKey(id)) {
            return mapSubTask.get(id);
        }
        return null;
    }
    public Epic getIdEpic(int id) {
        if(mapEpic.containsKey(id)) {
            return mapEpic.get(id);
        }
        return null;
    }
    //------------------------Создание. Сам объект должен передаваться в качестве параметра.-----------
    public void addTask(Task task) {
        if(!mapTask.containsValue(task)) {
            mapTask.put(id++,task);
        }
    }
    public void addSubTask(SubTask subtask) {
        if(!mapSubTask.containsValue(subtask)) {
            mapSubTask.put(id++,subtask);
        }
    }
    public void addEpic(Epic epic) {
        if(!mapEpic.containsValue(epic)) {
            mapEpic.put(id++,epic);
        }
    }
    //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
    public void updatingTask(int id, Task task) {
        mapTask.put(id,task);
    }
    public void updatingSubTask(int id, SubTask subTask) {
        mapSubTask.put(id,subTask);
    }
    public void updatingEpicTask(int id, Epic epic) {
        mapEpic.put(id,epic);
    }
    //-----------------------Удаление по идентификатору--------------------------
    public void deleteIdTask (int id) {
        mapTask.remove(id);
    }
    public void deleteIdSubTask (int id) {
        mapSubTask.remove(id);
    }
    public void deleteIdEpic (int id) {
        mapEpic.remove(id);
    }
    //------------------------- Получение списка всех подзадач определённого эпика-------------
    public List<SubTask> getListSubTaskEpic(int id) {
        Map<Integer,SubTask> MapSubTaskeer = mapEpic.get(id).getMapSubTask();
        return new ArrayList<>(MapSubTaskeer.values());
    }
}

