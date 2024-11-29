import java.util.*;

public class TaskManager {
    private Integer id = 0;

    private final Map<Integer, Task> mapTask;
    //Можно было связать еще так private final Map<Integer, Integer> mapEpicSubTask;
    //Мне из плюсов вспомнилось это Pair (там мы подобное с вектором проворачивали).
    //Усложнил все скорее всего (просто идея понравилась), не знаю какого эталона мы хотим достичь.
    private final Map<Integer, Pair<Epic, Map<Integer, SubTask>>> mapEpic = new HashMap<>();

    public TaskManager() {
        this.mapTask = new HashMap<>();
    }

    //------------------- Получение списка всех задач.------------------
    public List<Task> getAddTask() {
        return new ArrayList<>(mapTask.values());
    }

    public List<SubTask> getAddSubTask() {
        ArrayList<SubTask> subTask = new ArrayList<>();
        for (var epic : mapEpic.values()) {
            subTask.addAll(epic.value.values());
        }
        return subTask;
    }

    public List<Epic> getAddEpic() {
        ArrayList<Epic> epics = new ArrayList<>();
        for (var epic : mapEpic.values()) {
            epics.add(epic.key);
        }
        return epics;
    }

    //----------------------------Удаление всех задач.---------------------------
    public void deleteAllTask() {
        mapTask.clear();
    }

    public void deleteAllSubTask() {
        for (var id : mapEpic.keySet()) {
            mapEpic.get(id).value.clear();
            updatingEpicStatus(id);
        }
    }

    public void deleteAllEpic() {
        mapEpic.clear();
    }

    //---------------------Получение по идентификатору.----------------------------
    public Task getIdTask(int id) {
        if (mapTask.containsKey(id)) {
            return mapTask.get(id);
        }
        return null;
    }

    public SubTask getIdSubTask(int id) {
        for (var pair : mapEpic.values()) {
            if (pair.value.containsKey(id)) {
                return pair.value.get(id);
            }
        }
        return null;
    }

    public Epic getIdEpic(int id) {
        if (mapEpic.containsKey(id)) {
            return mapEpic.get(id).key;
        }
        return null;
    }

    //------------------------Создание. Сам объект должен передаваться в качестве параметра.-----------
    public void addTask(Task task) {
        if (!mapTask.containsValue(task)) {
            mapTask.put(id++, task);
        }
    }

    public void addSubTask(int idEpic, SubTask subtask) {
        if (mapEpic.containsKey(idEpic)) {
            if (!mapEpic.get(idEpic).value.containsValue(subtask)) {
                mapEpic.get(idEpic).value.put(id++, subtask);
            }
            updatingEpicStatus(idEpic);
        }
    }

    public void addEpic(Epic epic) {
        Pair<Epic, Map<Integer, SubTask>> pairEpic = new Pair<>(epic, new HashMap<>());
        if (!mapEpic.containsValue(pairEpic)) {
            mapEpic.put(id++, pairEpic);
        }
    }

    //---------Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.---
    public void updatingTask(int id, Task task) {
        mapTask.put(id, task);
    }

    public void updatingSubTask(int id, SubTask subTask) {
        for (var key : mapEpic.keySet()) {
            if (mapEpic.get(key).value.containsKey(id)) {
                mapEpic.get(key).value.put(id, subTask);
                updatingEpicStatus(key);
                return;
            }
        }
    }

    public void updatingEpicTask(int id, Epic epic) {
        mapEpic.put(id, new Pair<>(epic, new HashMap<>()));
    }

    //-----------------------Удаление по идентификатору--------------------------
    public void deleteIdTask(int id) {

        mapTask.remove(id);
    }

    public void deleteIdSubTask(int id) {
        for (var key : mapEpic.keySet()) {
            if (mapEpic.get(key).value.containsKey(id)) {
                mapEpic.get(key).value.remove(id);
                updatingEpicStatus(key);
                return;
            }
        }
    }

    public void deleteIdEpic(int id) {
        mapEpic.remove(id);
    }

    //------------------------- Получение списка всех подзадач определённого эпика-------------
    public List<SubTask> getListSubTaskEpic(int id) {
        Map<Integer, SubTask> result = mapEpic.get(id).value;
        return new ArrayList<SubTask>(result.values());
    }

    private void updatingEpicStatus(Integer idEpic) {
        Pair<Epic, Map<Integer, SubTask>> entry = mapEpic.get(idEpic);
        if (entry.value.isEmpty()) {
            entry.key.typeOfTask = TypeOfTask.NEW;
        } else {
            int count_done = 0;
            int count_new = 0;
            int size = entry.value.size();
            for (SubTask value : entry.value.values()) {
                if (value.typeOfTask == TypeOfTask.NEW) {
                    count_new++;
                }
                if (value.typeOfTask == TypeOfTask.DONE) {
                    count_done++;
                }
            }
            if (count_done == size) {
                entry.key.typeOfTask = TypeOfTask.DONE;
            } else if (count_new == size) {
                entry.key.typeOfTask = TypeOfTask.NEW;
            } else {
                entry.key.typeOfTask = TypeOfTask.IN_PROGRESS;
            }
        }

    }

}

