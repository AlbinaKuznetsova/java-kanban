package managers;

import java.util.Comparator;

import tasks.Task;

public class TaskComparator implements Comparator<Task> {

    @Override
    public int compare(Task o1, Task o2) {
        if (o1.getStartTime() == null && o2.getStartTime() == null) {
            return o1.getId().compareTo(o2.getId());
        }
        if (o1.getStartTime() == null) {
            return 1;
        } else if (o2.getStartTime() == null) {
            return -1;
        } else if (o1.getStartTime().equals(o2.getStartTime())) {
            return o1.getId().compareTo(o2.getId());
        } else {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }
} 