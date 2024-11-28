import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    public Epic(String taskDescription,String taskDetails, TypeOfTask typeOfTask) {
        super(taskDescription,taskDetails,typeOfTask);

    }
    private final Map<Integer,SubTask> MapSubTask = new HashMap<>();

    public Map<Integer,SubTask> getMapSubTask() {
        return MapSubTask;
    }
    public void setSubTask(Integer id, SubTask subTask) {
        MapSubTask.put(id,subTask);
        int count_done = 0;
        int count_new = 0;
        int size = MapSubTask.size();
        for (SubTask value : MapSubTask.values()) {
            if(value.typeOfTask == TypeOfTask.NEW){
                count_new++;
            }
            if(value.typeOfTask == TypeOfTask.DONE){
                count_done++;
            }
        }
        if(count_done == size){
            this.typeOfTask = TypeOfTask.DONE;
        } else if (count_new == size) {
            this.typeOfTask = TypeOfTask.NEW;
        }else {
            this.typeOfTask = TypeOfTask.IN_PROGRESS;
        }
    }

}



