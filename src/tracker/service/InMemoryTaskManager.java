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
    //Managers по сути фабрика
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.mapTask = new HashMap<>();
        this.mapEpic = new HashMap<>();
        this.mapSubTask = new HashMap<>();
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
        subtask.setId(id);
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
    //по логике и из истории они отлетают
    @Override
    public void deleteAllTask() {
        for (Integer integer : mapTask.keySet()) {
            inMemoryHistoryManager.remove(integer);
        }
        mapTask.clear();
    }

    @Override
    public void deleteAllSubTask() {
        for (Integer idEpic : mapEpic.keySet()) {
            mapEpic.get(idEpic).getIdAllSubTask().clear();
            updatingEpicStatus(idEpic);
        }
        for (Integer integer : mapSubTask.keySet()) {
            inMemoryHistoryManager.remove(integer);
        }
        mapSubTask.clear();
    }

    @Override
    public void deleteAllEpic() {
        deleteAllSubTask();
        for (Integer integer : mapEpic.keySet()) {
            inMemoryHistoryManager.remove(integer);
        }
        mapEpic.clear();
    }

    //---------------------Получение по идентификатору.----------------------------
    @Override
    public Task getIdTask(Integer id) {
        inMemoryHistoryManager.add(mapTask.get(id));
        return mapTask.get(id);
    }

    @Override
    public SubTask getIdSubTask(Integer id) {
        inMemoryHistoryManager.add(mapSubTask.get(id));
        return mapSubTask.get(id);
    }

    @Override
    public Epic getIdEpic(Integer id) {
        inMemoryHistoryManager.add(mapEpic.get(id));
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
                subTask.setId(idEpic);
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
        inMemoryHistoryManager.remove(id);
    }

    @Override
    public void deleteIdSubTask(Integer id) {
        for (Integer idEpic : mapEpic.keySet()) {
            var result = mapEpic.get(idEpic).getIdAllSubTask();
            if (result.contains(id)) {
                result.remove(id);
                inMemoryHistoryManager.remove(id);
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
            inMemoryHistoryManager.remove(idSubTask);
        }
        mapEpic.remove(id);
        inMemoryHistoryManager.remove(id);
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
            int countDone = 0;
            int countNew = 0;
            int size = mapEpic.get(idEpic).getIdAllSubTask().size();
            for (Integer value : mapEpic.get(idEpic).getIdAllSubTask()) {
                if (mapSubTask.get(value).getTypeOfTask() == TypeOfTask.NEW) {
                    countNew++;
                }
                if (mapSubTask.get(value).getTypeOfTask() == TypeOfTask.DONE) {
                    countDone++;
                }
            }
            if (countDone == size) {
                mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.DONE);
            } else if (countNew == size) {
                mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.NEW);
            } else {
                mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.IN_PROGRESS);
            }
        } else {
            mapEpic.get(idEpic).setTypeOfTask(TypeOfTask.NEW);
        }
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
