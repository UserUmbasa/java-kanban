package tracker.service;
import tracker.model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public static File FileBackedTaskManager() {
        return new File("History.csv");
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(int idEpic, SubTask subtask) {
        super.addSubTask(idEpic, subtask);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void updatingTask(Integer id, Task task) {
        super.updatingTask(id, task);
        save();
    }

    @Override
    public void updatingSubTask(Integer id, SubTask subTask) {
        super.updatingSubTask(id, subTask);
        save();
    }

    @Override
    public void updatingEpicTask(Integer id, Epic epic) {
        super.updatingEpicTask(id, epic);
        save();
    }

    @Override
    public void deleteIdTask(Integer id) {
        super.deleteIdTask(id);
        save();
    }

    @Override
    public void deleteIdSubTask(Integer id) {
        super.deleteIdSubTask(id);
        save();
    }

    @Override
    public void deleteIdEpic(Integer id) {
        super.deleteIdEpic(id);
        save();
    }

    //-------------------------------Сохранение-----------------------------------
    public void save() {
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) { //объект потока для потока - вывода для записи символов в файл
            fileWriter.write(this.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        List<String> resultTask = breakTaskLines(mapTask);
        List<String> resultEpic = breakTaskLines(mapEpic);
        List<String> resultSubTusk = breakTaskLines(mapSubTask);
        for (String s : resultTask) {
            result.append(s).append("\n");
        }
        for (String s : resultEpic) {
            result.append(s).append("\n");
        }
        for (String s : resultSubTusk) {
            result.append(s).append("\n");
        }
        result.append(id);
        return result.toString();
    }

    private List<String> breakTaskLines(Map<Integer, ? extends Task> map) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<Integer, ? extends Task> integerTaskEntry : map.entrySet()) {
            //класс->айдиМапы->айдиЗадачи->имя->Описание->Статус->Подзадачи(Епик)
            List<String> strings = new ArrayList<>();
            Task task = integerTaskEntry.getValue();
            strings.add(task.getClass().getSimpleName());
            strings.add(integerTaskEntry.getKey().toString());
            strings.add(task.getId().toString());
            strings.add(task.getTaskDescription());
            strings.add(task.getTaskDetails());
            strings.add(task.getTypeOfTask().toString());
            if(task instanceof Epic) {
                Epic epic = (Epic) integerTaskEntry.getValue();
                for (Integer integer : epic.getIdAllSubTask()) {
                    strings.add(integer.toString());
                }
            }
            result.add(String.join(",", strings));
        }
        return result;
    }

    //-----------------------------загрузка-------------------------------
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        List<String> tasks = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                tasks.add(line);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        fillData(tasks, fileBackedTaskManager);
        return fileBackedTaskManager;
    }

    //отделили логику заполнения
    private static void fillData(List<String> tasks, FileBackedTaskManager fileBackedTaskManager) {
        // тип->айдиМапа->айдиЗадачи->название->описание->статус
        // конструктор таска название описание статус
        if (!tasks.isEmpty()) {
            for (String task : tasks) {
                String[] split = task.split(",");
                if (split.length == 1) fileBackedTaskManager.id = Integer.parseInt(split[0]);
                switch (split[0]) {
                    //нет общего интерфейса (не получится сделать одним методом)
                    case "Task" -> {
                        Task temp = new Task(split[3], split[4], TypeOfTask.valueOf(split[5]));
                        temp.setId(Integer.parseInt(split[2]));
                        fileBackedTaskManager.mapTask.put(Integer.parseInt(split[1]), temp);
                    }
                    case "Epic" -> {
                        Epic temp = new Epic(split[3], split[4], TypeOfTask.valueOf(split[5]));
                        temp.setId(Integer.parseInt(split[2]));
                        fileBackedTaskManager.mapEpic.put(Integer.parseInt(split[1]), temp);
                        for (int i = 6; i < split.length; i++) {
                            temp.getIdAllSubTask().add(Integer.parseInt(split[i]));
                        }
                    }
                    case "SubTask" -> {
                        SubTask temp = new SubTask(split[3], split[4], TypeOfTask.valueOf(split[5]));
                        temp.setId(Integer.parseInt(split[2]));
                        fileBackedTaskManager.mapSubTask.put(Integer.parseInt(split[1]), temp);
                    }
                }
            }
        }
    }
}
