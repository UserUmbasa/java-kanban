package tracker.service;

import tracker.model.Task;


import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    //----история просмотренных задач-------
    protected List<Task> historyTask = new LinkedList<>();
    @Override
    public void add(Task task) {
        if (task != null) {
            historyTask.add(task);
            if (historyTask.size()>10) {
                historyTask.removeFirst();
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(historyTask);
    }
}
