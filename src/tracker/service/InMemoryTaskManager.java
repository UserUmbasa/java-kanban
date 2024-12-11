package tracker.service;

import tracker.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;
    private final Map<Integer, Task> mapTask;
    private final Map<Integer, Epic> mapEpic;
    private final Map<Integer, SubTask> mapSubTask;
    //----история просмотренных задач-------
    HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.mapTask = new HashMap<>();
        this.mapEpic = new HashMap<>();
        this.mapSubTask = new HashMap<>();
        this.historyManager = historyManager;
    }
    //------------------------Создание. Сам объект должен передаваться в качестве параметра.-----------

    @Override
    public void addTask(Task task) {
        if (!mapTask.containsValue(task)) {
            task.setId(id);
            mapTask.put(id++, task);
        }
    }

    @Override
    public void addSubTask(int idEpic, SubTask subtask) {
        //Для каждой подзадачи известно, в рамках какого эпика она выполняется
        subtask.setId(idEpic);
        if (mapEpic.containsKey(idEpic) && !mapSubTask.containsValue(subtask)) {
            mapEpic.get(idEpic).getIdAllSubTask().add(id);
            mapSubTask.put(id++, subtask);
            updatingEpicStatus(idEpic);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (!mapEpic.containsValue(epic)) {
            epic.setId(id);
            mapEpic.put(id++, epic);
        }
    }

    //------------------- Получение списка всех задач.------------------
    @Override
    public List<Task> getAddTask() {
        return new ArrayList<>(mapTask.values());
    }

    @Override
    public List<SubTask> getAddSubTask() {
        return new ArrayList<>(mapSubTask.values());
    }

    @Override
    public List<Epic> getAddEpic() {
        return new ArrayList<>(mapEpic.values());
    }

    //----------------------------Удаление всех задач.---------------------------
    @Override
    public void deleteAllTask() {
        mapTask.clear();
    }

    @Override
    public void deleteAllSubTask() {
        for (Integer idEpic : mapEpic.keySet()) {
            mapEpic.get(idEpic).getIdAllSubTask().clear();
            updatingEpicStatus(idEpic);
        }
        mapSubTask.clear();
    }

    @Override
    public void deleteAllEpic() {
        mapSubTask.clear();
        mapEpic.clear();
    }

    //---------------------Получение по идентификатору.----------------------------
    @Override
    public Task getIdTask(Integer id) {
        historyManager.add(mapTask.get(id));
        return mapTask.get(id);
    }

    @Override
    public SubTask getIdSubTask(Integer id) {
        historyManager.add(mapSubTask.get(id));
        return mapSubTask.get(id);
    }

    @Override
    public Epic getIdEpic(Integer id) {
        historyManager.add(mapEpic.get(id));
        return mapEpic.get(id);
    }


    //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
    @Override
    public void updatingTask(Integer id, Task task) {
        task.setId(id);
        mapTask.put(id, task);
    }

    @Override
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

    @Override
    public void updatingEpicTask(Integer id, Epic epic) {
        deleteIdEpic(id);
        epic.setId(id);
        mapEpic.put(id, epic);
    }

    //-----------------------Удаление по идентификатору--------------------------
    @Override
    public void deleteIdTask(Integer id) {
        mapTask.remove(id);
    }

    @Override
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

    @Override
    public void deleteIdEpic(Integer id) {
        for (Integer idSubTask : mapEpic.get(id).getIdAllSubTask()) {
            mapSubTask.remove(idSubTask);
        }
        mapEpic.remove(id);
    }

    //------------------------- Получение списка всех подзадач определённого эпика-------------
    @Override
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
