package managers;

import managers.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    //protected List<Task> taskHistory = new ArrayList<>();
    private class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private Map<Integer,Node> map = new HashMap<>();
    private Node first;
    private Node last;

    protected void linkLast(Task task) {
        if (last == null) {
            first = new Node(task, null, null);
            last = first;
        } else {
            Node lastOld = last;
            last = new Node(task, lastOld, null);
            lastOld.next = last;
        }
    }
    protected ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node node = first;
        while(node.next != null) {
            tasks.add(node.task);
            node = node.next;
        }
        tasks.add(node.task); // добавили хвост в список
        return tasks;
    }
    protected void removeNode(Node node) {
        if (node.next == null) {
            node.prev.next = null;
            last = node.prev;
        }
        if (node.prev == null) {
            node.next.prev = null;
            first = node.next;
        }
        if (node.prev != null && node.next != null) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

    }
    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
        map.put(task.getId(), last);
    }
    public void remove(int id) {
        Node node = map.get(id);
        if (node != null) {
            removeNode(node);
        }
    }
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}
