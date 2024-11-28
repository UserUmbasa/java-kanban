import java.util.ArrayList;
import java.util.List;

public class SubTask extends Task {
    public SubTask(String taskDescription,String taskDetails,TypeOfTask typeOfTask) {
        super(taskDescription, taskDetails,typeOfTask);
    }
    private final List<Task> listTask = new ArrayList<>();
    public void addTask(Task task) {
        listTask.add(task);
    }

    public List<Task> getListTask() {
        return listTask;
    }

}