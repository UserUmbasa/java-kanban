package tracker.service;

import tracker.model.Epic;
import tracker.model.SubTask;
import tracker.model.Task;
import tracker.model.TypeOfTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private Integer id = 0;
    private final Map<Integer, Task> mapTask;
    private final Map<Integer, Epic> mapEpic;
    private final Map<Integer, SubTask> mapSubTask;

    public TaskManager() {
        this.mapTask = new HashMap<>();
        this.mapEpic = new HashMap<>();
        this.mapSubTask = new HashMap<>();
    }
    //------------------------Создание. Сам объект должен передаваться в качестве параметра.-----------

    public void addTask(Task task) {
        if (!mapTask.containsValue(task)) {
            task.setId(id);
            mapTask.put(id++, task);
        }
    }

    public void addSubTask(int idEpic, SubTask subtask) {
        if (mapEpic.containsKey(idEpic) && !mapSubTask.containsValue(subtask)) {
            subtask.setId(idEpic);//Для каждой подзадачи известно, в рамках какого эпика она выполняется.
            mapEpic.get(idEpic).getIdAllSubTask().add(id);
            mapSubTask.put(id++, subtask);
            updatingEpicStatus(idEpic);
        }
    }

    public void addEpic(Epic epic) {
        if (!mapEpic.containsValue(epic)) {
            epic.setId(id);
            mapEpic.put(id++, epic);
        }
    }

    //------------------- Получение списка всех задач.------------------
    public List<Task> getAddTask() {
        return new ArrayList<>(mapTask.values());
    }

    public List<SubTask> getAddSubTask() {
        return new ArrayList<>(mapSubTask.values());
    }

    public List<Epic> getAddEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    //----------------------------Удаление всех задач.---------------------------
    public void deleteAllTask() {
        mapTask.clear();
    }

    public void deleteAllSubTask() {
        for (Integer idEpic : mapEpic.keySet()) {
            mapEpic.get(idEpic).getIdAllSubTask().clear();
            updatingEpicStatus(idEpic);
        }
        mapSubTask.clear();
    }

    public void deleteAllEpic() {
        mapSubTask.clear();
        mapEpic.clear();
    }

    //---------------------Получение по идентификатору.----------------------------
    public Task getIdTask(Integer id) {
        return mapTask.get(id);
    }

    public SubTask getIdSubTask(Integer id) {
        return mapSubTask.get(id);
    }

    public Epic getIdEpic(Integer id) {
        return mapEpic.get(id);
    }


    //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
    public void updatingTask(Integer id, Task task) {
        task.setId(id);
        mapTask.put(id, task);
    }

    public void updatingSubTask(Integer id, SubTask subTask) {
        mapSubTask.put(id, subTask);
        for (Integer idEpic : mapEpic.keySet()) {
            if (mapEpic.get(idEpic).getIdAllSubTask().contains(id)) {
                subTask.setId(idEpic); //знает кому принадлежит (приходит ли он с верным внутренним айди, не ясно)
                updatingEpicStatus(idEpic);
                break;
            }
        }
    }

    public void updatingEpicTask(Integer id, Epic epic) {
        deleteIdEpic(id);
        epic.setId(id);
        mapEpic.put(id, epic);
    }

    //-----------------------Удаление по идентификатору--------------------------
    public void deleteIdTask(Integer id) {
        mapTask.remove(id);
    }

    public void deleteIdSubTask(Integer id) {
        for (Integer idEpic : mapEpic.keySet()) {
            var result = mapEpic.get(idEpic).getIdAllSubTask();
            if (result.contains(id)) {
                result.remove(id);
                updatingEpicStatus(idEpic);
                break;
            }
        }
        mapSubTask.remove(id);
    }

    public void deleteIdEpic(Integer id) {
        for (Integer idSubTask : mapEpic.get(id).getIdAllSubTask()) {
            mapSubTask.remove(idSubTask);
        }
        mapEpic.remove(id);
    }

    //------------------------- Получение списка всех подзадач определённого эпика-------------
    public List<SubTask> getListSubTaskEpic(Integer id) {
        List<SubTask> result = new ArrayList<>();
        if (mapEpic.containsKey(id)) {
            for (Integer idSubTask : mapEpic.get(id).getIdAllSubTask()) {
                result.add(mapSubTask.get(idSubTask));
            }
        }
        return result;
    }

    private void updatingEpicStatus(Integer idEpic) {
        if (!mapEpic.get(idEpic).getIdAllSubTask().isEmpty()) {
            int count_done = 0;
            int count_new = 0;
            int size = mapEpic.get(idEpic).getIdAllSubTask().size();
            for (Integer value : mapEpic.get(idEpic).getIdAllSubTask()) {
                if (mapSubTask.get(value).getTypeOfTask() == TypeOfTask.NEW) {
                    count_new++;
                }
                if (mapSubTask.get(value).getTypeOfTask() == TypeOfTask.DONE) {
                    count_done++;
                }
            }
            if (count_done == size) {
                mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.DONE);
            } else if (count_new == size) {
                mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.NEW);
            } else {
                mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.IN_PROGRESS);
            }
        } else {
            mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.NEW);
        }
    }
}
