package tracker.model;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String taskDescription;
    protected String taskDetails;
    protected TypeOfTask typeOfTask;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypeOfTask getTypeOfTask() {
        return typeOfTask;
    }

    public void setTypeOfTask(TypeOfTask typeOfTask) {
        this.typeOfTask = typeOfTask;
    }

    public Task(String taskDescription, String taskDetails, TypeOfTask typeOfTask) {
        this.taskDescription = taskDescription;
        this.typeOfTask = typeOfTask;
        this.taskDetails = taskDetails;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskDetails='" + taskDetails + '\'' +
                ", typeOfTask=" + typeOfTask +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(taskDescription, task.taskDescription) && Objects.equals(taskDetails, task.taskDetails) && typeOfTask == task.typeOfTask;
    }

    @Override
    public final int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(taskDescription);
        result = 31 * result + Objects.hashCode(taskDetails);
        result = 31 * result + Objects.hashCode(typeOfTask);
        return result;
    }
}



