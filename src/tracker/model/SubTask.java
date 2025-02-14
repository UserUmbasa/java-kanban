package tracker.model;

public class SubTask extends Task {
    public SubTask(String taskDescription, String taskDetails, TypeOfTask typeOfTask, String time, Long duration) {
        super(taskDescription, taskDetails,typeOfTask, time, duration);
        taskClassifier = TaskClassifier.SUBTASK;
    }

    public SubTask(String taskDescription, String taskDetails, TypeOfTask typeOfTask) {
        super(taskDescription, taskDetails,typeOfTask);
        taskClassifier = TaskClassifier.SUBTASK;
    }
}