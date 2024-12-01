package tracker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    public Epic(String taskDescription, String taskDetails, TypeOfTask typeOfTask) {
        super(taskDescription, taskDetails, typeOfTask);

    }

    public List<Integer> getIdAllSubTask() {
        return idSubTask;
    }
    //не противоречит замечанию: Никакого хранилища объектов внутри класса подзадач не должно быть
    private final List<Integer> idSubTask = new ArrayList<>();


}