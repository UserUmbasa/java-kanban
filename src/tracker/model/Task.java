package tracker.model;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String taskDescription;
    protected String taskDetails;
    protected TypeOfTask typeOfTask;
    protected TaskClassifier taskClassifier;

    public TaskClassifier getTaskClass() {
        return taskClassifier;
    }
    
    public StringBuilder getFormatCsv(int idMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(taskClassifier).append(',').append(idMap).append(',').append(id).append(',');
        sb.append(taskDescription).append(',').append(taskDetails).append(',').append(typeOfTask);
        return sb;
    }

    public Integer getId() {
        return id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskDetails() {
        return taskDetails;
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
        taskClassifier = TaskClassifier.TASK;
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



