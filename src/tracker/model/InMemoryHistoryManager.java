package tracker.model;

import tracker.service.HistoryManager;


import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //----история просмотренных задач-------
    List<Task> historyTask = new LinkedList<>();
    @Override
    public void add(Task task) {
        if (historyTask.size()>=10) {
            historyTask.removeFirst();
            historyTask.add(task);
        }else{
            historyTask.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyTask;
    }
}
