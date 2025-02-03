package tracker.model;


public class SubTask extends Task {
    public SubTask(String taskDescription, String taskDetails, TypeOfTask typeOfTask) {
        super(taskDescription, taskDetails,typeOfTask);
        taskClassifier = TaskClassifier.SUBTASK;
    }
}