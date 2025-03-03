package tracker.model;

public class SubTask extends Task {

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }

    private int idEpic = 0;

    public SubTask(String taskDescription, String taskDetails, TypeOfTask typeOfTask, String time, Long duration) {
        super(taskDescription, taskDetails,typeOfTask, time, duration);
        taskClassifier = TaskClassifier.SUBTASK;
    }

    public SubTask(String taskDescription, String taskDetails, TypeOfTask typeOfTask) {
        super(taskDescription, taskDetails,typeOfTask);
        taskClassifier = TaskClassifier.SUBTASK;
    }
}