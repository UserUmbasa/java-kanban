
import java.util.Objects;

public class Task {
    protected String taskDescription;
    protected String taskDetails;
    protected TypeOfTask typeOfTask;
    public Task(String taskDescription,String taskDetails,TypeOfTask typeOfTask) {
        this.taskDescription = taskDescription;
        this.typeOfTask = typeOfTask;
        this.taskDetails = taskDetails;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskDescription='" + taskDescription + '\'' +
                ", taskDetails='" + taskDetails + '\'' +
                ", typeOfTask=" + typeOfTask +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(taskDescription, task.taskDescription) && Objects.equals(taskDetails, task.taskDetails) && typeOfTask == task.typeOfTask;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(taskDescription);
        result = 31 * result + Objects.hashCode(taskDetails);
        result = 31 * result + Objects.hashCode(typeOfTask);
        return result;
    }
}



