package managers;

import managers.HistoryManager;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;

public class Managers {
    public static TaskManager getDefault() {
        TaskManager manager = new InMemoryTaskManager();
        return manager;
    }
    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
