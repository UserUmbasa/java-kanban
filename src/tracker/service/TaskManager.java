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
    //------------------------Создание. Сам объект должен передаваться в качестве параметра.(нужно вернуть его)-----------

    public Task addTask(Task task) {
        if (!mapTask.containsValue(task)) {
            task.setId(id);
            mapTask.put(id++, task);
        }else {
            for (Task value : mapTask.values()) {
                if (value.equals(task)) {
                    return value;
                }
            }
        }
        return task;
    }

    public SubTask addSubTask(int idEpic, SubTask subtask) {
        if (mapEpic.containsKey(idEpic)) {
            if (!mapSubTask.containsValue(subtask)) {
                subtask.setId(id);
                mapEpic.get(idEpic).getIdAllSubTask().add(id);
                mapSubTask.put(id++, subtask);
                updatingEpicStatus(idEpic);
            }else {
                for (SubTask value : mapSubTask.values()) {
                    if (value.equals(subtask)) {
                        return value;
                    }
                }
            }
        }
        return subtask;
    }

    public Epic addEpic(Epic epic) {
        if (!mapEpic.containsValue(epic)) {
            epic.setId(id);
            mapEpic.put(id++, epic);
        }else {
            for (Epic value : mapEpic.values()) {
                if (value.equals(epic)) {
                    return value;
                }
            }
        }
        return epic;
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
    public List<Task> deleteAllTask() {
        List<Task> result = new ArrayList<>(mapTask.values());
        mapTask.clear();
        return result;
    }

    public List<SubTask> deleteAllSubTask() {
        List<SubTask> result = new ArrayList<>(mapSubTask.values());
        for (Integer idEpic : mapEpic.keySet()) {
            mapEpic.get(idEpic).getIdAllSubTask().clear();
            updatingEpicStatus(idEpic);
        }
        mapSubTask.clear();
        return result;
    }

    public List<Epic> deleteAllEpic() {
        List<Epic>result = new ArrayList<>(mapEpic.values());
        mapSubTask.clear();
        mapEpic.clear();
        return result;
    }

    //---------------------Получение по идентификатору.----------------------------
    public Task getIdTask(Integer id) {
        if (mapTask.containsKey(id)) {
            return mapTask.get(id);
        }
        return null;
    }

    public SubTask getIdSubTask(Integer id) {
        if (mapSubTask.containsKey(id)) {
            return mapSubTask.get(id);
        }
        return null;
    }

    public Epic getIdEpic(Integer id) {
        if (mapEpic.containsKey(id)) {
            return mapEpic.get(id);
        }
        return null;
    }


    //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
    public Task updatingTask(Integer id, Task task) {
        task.setId(id);
        mapTask.put(id, task);
        return task;
    }

    public SubTask updatingSubTask(Integer id, SubTask subTask) {
        subTask.setId(id);
        mapSubTask.put(id, subTask);
        for (Integer idEpic : mapEpic.keySet()) {
            if (mapEpic.get(idEpic).getIdAllSubTask().contains(id)) {
                updatingEpicStatus(idEpic);
                break;
            }
        }
        return subTask;
    }

    public Epic updatingEpicTask(Integer id, Epic epic) {
        deleteIdEpic(id);
        epic.setId(id);
        mapEpic.put(id, epic);
        return epic;
    }

    //-----------------------Удаление по идентификатору--------------------------
    public Task deleteIdTask(Integer id) {
        Task task = mapTask.get(id);
        mapTask.remove(id);
        return task;
    }

    public SubTask deleteIdSubTask(Integer id) {
        SubTask subTask = mapSubTask.get(id);
        for (Integer idEpic : mapEpic.keySet()) {
            if (mapEpic.get(idEpic).getIdAllSubTask().contains(id)) {
                mapEpic.get(idEpic).getIdAllSubTask().remove(id);
                updatingEpicStatus(idEpic);
                break;
            }
        }
        mapSubTask.remove(id);
        return subTask;
    }

    public Epic deleteIdEpic(Integer id) {
        Epic epic = mapEpic.get(id);
        for (Integer idSubTask : mapEpic.get(id).getIdAllSubTask()) {
            mapSubTask.remove(idSubTask);
        }
        mapEpic.remove(id);
        return epic;
    }

    //------------------------- Получение списка всех подзадач определённого эпика-------------
    public List<SubTask> getListSubTaskEpic(Integer id) {
        if (mapEpic.containsKey(id)) {
            List<SubTask> result = new ArrayList<>();
            for (Integer idSubTask : mapEpic.get(id).getIdAllSubTask()) {
                result.add(mapSubTask.get(idSubTask));
            }
            return result;
        }
        return null;
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
