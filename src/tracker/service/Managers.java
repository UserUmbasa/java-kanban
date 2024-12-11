package tracker.service;

import tracker.model.InMemoryHistoryManager;

public class Managers {

    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private  final TaskManager taskManager = new InMemoryTaskManager(historyManager);
    public TaskManager getDefault(){
        return taskManager;
    }
    public static HistoryManager getDefaultHistory (){
        return historyManager;
    }
}
