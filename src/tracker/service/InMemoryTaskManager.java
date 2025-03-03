package tracker.service;

import tracker.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected Integer id = 0;
    protected int code = 0;
    protected final Map<Integer, Task> mapTask;
    protected final Map<Integer, Epic> mapEpic;
    protected final Map<Integer, SubTask> mapSubTask;
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> task1.getTimeStart().compareTo(task2.getTimeStart()));
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.mapTask = new HashMap<>();
        this.mapEpic = new HashMap<>();
        this.mapSubTask = new HashMap<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    //------------------------Создание. Сам объект должен передаваться в качестве параметра.----------
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(this.prioritizedTasks);
    }

    @Override
    public void addTask(Task task) {
        if (!mapTask.containsValue(task)) {
            task.setId(id);
            if (task.getTimeStart() == null || !getPrioritizedTasks(task)) {
                mapTask.put(id++, task);
                if (task.getTimeStart() != null) {
                    prioritizedTasks.add(task);
                }
                code = 201;
            } else {
                code = 406;
            }
        } else {
            code = 406;
        }
    }

    @Override
    public void addSubTask(int idEpic, SubTask subtask) {
        //мне кажется я скоро перестану понимать что тут происходит =)
        subtask.setId(id);
        if ((mapEpic.containsKey(idEpic) && !mapSubTask.containsValue(subtask)) &&
                (subtask.getTimeStart() == null || !getPrioritizedTasks(subtask))) {
            mapEpic.get(idEpic).getIdAllSubTask().add(id);
            mapSubTask.put(id++, subtask);
            code = 201;
            if (subtask.getTimeStart() == null) {
                return;
            }
            prioritizedTasks.add(subtask);
            updatingEpicStatus(idEpic);
            updatingEpicTime(getIdEpic(idEpic));
        } else {
            code = 406;
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (!mapEpic.containsValue(epic)) {
            epic.setId(id);
            mapEpic.put(id++, epic);
            code = 201;
        } else {
            code = 406;
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
        for (Map.Entry<Integer,Task> entry : mapTask.entrySet()) {
            inMemoryHistoryManager.remove(entry.getKey());
            // чистим приоритет
            if (entry.getValue().getTimeStart() != null) {
                prioritizedTasks.remove(entry.getValue());
            }
        }
        mapTask.clear();
    }

    @Override
    public void deleteAllSubTask() {
        for (Integer idEpic : mapEpic.keySet()) {
            var epic = mapEpic.get(idEpic);
            epic.getIdAllSubTask().clear();
            updatingEpicStatus(idEpic);
        }
        for (Map.Entry<Integer,SubTask> entry : mapSubTask.entrySet()) {
            inMemoryHistoryManager.remove(entry.getKey());
            if (entry.getValue().getTimeStart() != null) {
                prioritizedTasks.remove(entry.getValue());
            }
        }
        for (Integer idEpic : mapEpic.keySet()) {
            updatingEpicTime(mapEpic.get(idEpic));
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
        if (mapTask.containsKey(id)) {
            inMemoryHistoryManager.add(mapTask.get(id));
            return mapTask.get(id);
        }
        return null;
    }

    @Override
    public SubTask getIdSubTask(Integer id) {
        if (mapSubTask.containsKey(id)) {
            inMemoryHistoryManager.add(mapSubTask.get(id));
            return mapSubTask.get(id);
        }
        return null;
    }

    @Override
    public Epic getIdEpic(Integer id) {
        if (mapEpic.containsKey(id)) {
            inMemoryHistoryManager.add(mapEpic.get(id));
            return mapEpic.get(id);
        }
        return null;
    }

    //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
    @Override
    public void updatingTask(Integer id, Task task) {
        updateAssistant(id,task,mapTask);
    }

    @Override
    public void updatingSubTask(Integer id, SubTask subTask) {
        updateAssistant(id,subTask,mapSubTask);
        for (Integer idEpic : mapEpic.keySet()) {
            Epic epic = mapEpic.get(idEpic);
            if (epic.getIdAllSubTask().contains(id)) {
                updatingEpicStatus(idEpic);
                updatingEpicTime(epic);
                break;
            }
        }
    }

    private <T extends Task> void updateAssistant(Integer id, T subTask, Map<Integer, T> map) {
        subTask.setId(id);
        var taskTemp = map.get(id);
        if (taskTemp.getTimeStart() == null && subTask.getTimeStart() != null) {
            if (!getPrioritizedTasks(subTask)) {
                map.put(id, subTask);
                prioritizedTasks.add(subTask);
            } else {
                code = 406;
            }
        } else if (taskTemp.getTimeStart() != null && subTask.getTimeStart() == null) {
            prioritizedTasks.remove(taskTemp);
            map.put(id, subTask);
        } else if (taskTemp.getTimeStart() != null && subTask.getTimeStart() != null) {
            prioritizedTasks.remove(taskTemp);
            if (!getPrioritizedTasks(subTask)) {
                map.put(id, subTask);
                prioritizedTasks.add(subTask);
            } else {
                code = 406;
                prioritizedTasks.add(taskTemp);
            }
        } else {
            map.put(id, subTask);
        }
        code = 201;
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
        if (mapTask.containsKey(id)) {
            var task = mapTask.get(id);
            if (task.getTimeStart() != null) prioritizedTasks.remove(task);
            mapTask.remove(id);
            inMemoryHistoryManager.remove(id);
        }
    }

    @Override
    public void deleteIdSubTask(Integer id) {
        for (Integer idEpic : mapEpic.keySet()) {
            Epic epic = mapEpic.get(idEpic);
            var result = epic.getIdAllSubTask();
            if (result.contains(id)) {
                var subTask = mapSubTask.get(id);
                if (subTask.getTimeStart() != null) {
                    prioritizedTasks.remove(subTask);
                }
                result.remove(id);
                inMemoryHistoryManager.remove(id);
                mapSubTask.remove(id);
                updatingEpicStatus(idEpic);
                updatingEpicTime(epic);
                break;
            }
        }
    }

    @Override
    public void deleteIdEpic(Integer id) {
        if (mapEpic.containsKey(id)) {
            for (Integer idSubTask : mapEpic.get(id).getIdAllSubTask()) {
                var subTask = mapSubTask.get(idSubTask);
                if (subTask.getTimeStart() != null) {
                    prioritizedTasks.remove(subTask);
                }
                mapSubTask.remove(idSubTask);
                inMemoryHistoryManager.remove(idSubTask);
            }
            mapEpic.remove(id);
            inMemoryHistoryManager.remove(id);
        }
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

    private void updatingEpicTime(Epic epic) {
        TreeSet<SubTask> prioritized = new TreeSet<>((task1, task2) -> task1.getTimeStart().compareTo(task2.getTimeStart()));
        for (Map.Entry<Integer,SubTask> entry : mapSubTask.entrySet()) {
            if (entry.getValue().getTaskClass() == TaskClassifier.SUBTASK && entry.getValue().getTimeStart() != null) {
                prioritized.add(entry.getValue());
            }
        }
        if (prioritized.isEmpty()) {
            epic.setTimeStart(null);
            epic.setTimeEnd(null);
            epic.setDuration(null);
            return;
        }
        epic.setTimeStart(prioritized.first().getTimeStart());
        epic.setTimeEnd(prioritized.last().getTimeEnd());
        epic.setDuration(Duration.between(epic.getTimeStart(), epic.getTimeEnd()));
    }

    protected boolean getPrioritizedTasks(Task task) {
        if (!prioritizedTasks.isEmpty()) {
            // если конец задачи добавляемой задачи раньше, чем начало первой задачи в массиве
            if (task.getTimeEnd().isBefore(prioritizedTasks.first().getTimeStart()) ||
                    task.getTimeEnd().equals(prioritizedTasks.first().getTimeStart())) {
                return false;
            }
            // если начало добавляемой задачи позже, чем конец последней задачи в массиве
            if (task.getTimeStart().isAfter(prioritizedTasks.getLast().getTimeEnd()) ||
                    task.getTimeStart().equals(prioritizedTasks.getLast().getTimeEnd())) {
                return false;
            }
            // иначе середина, либо тру
            LocalDateTime count = prioritizedTasks.first().getTimeStart();
            for (Task prioritizedTask : prioritizedTasks) {
                LocalDateTime timeStart = prioritizedTask.getTimeStart();
                LocalDateTime timeEnd = prioritizedTask.getTimeEnd();
                // если конец текущей задачи меньше или равно, чем начало добавляемой (запоминаем и идем дальше)
                if (timeEnd.isBefore(task.getTimeStart()) || timeEnd.equals(task.getTimeStart())) {
                    count = timeEnd;
                } else {
                    // если больше, либо равна, то смотрим, позволяет ли интервал
                    return !task.getTimeEnd().isBefore(timeStart) && !task.getTimeEnd().equals(timeStart);
                }
            }
        }
        return false;
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
