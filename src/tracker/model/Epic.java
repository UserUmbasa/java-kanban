package tracker.model;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public Epic(String taskDescription, String taskDetails, TypeOfTask typeOfTask) {
        super(taskDescription, taskDetails, typeOfTask);
        taskClassifier = TaskClassifier.EPIC;
    }

    public List<Integer> getIdAllSubTask() {
        return idSubTask;
    }

    private final List<Integer> idSubTask = new ArrayList<>();
}