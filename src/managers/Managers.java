package managers;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078/");
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
