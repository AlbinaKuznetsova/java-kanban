package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
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

    private Map<Integer, Node> map = new HashMap<>();
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
        Node node = last;
        if (node != null) {
            while (node.prev != null) {
                tasks.add(node.task);
                node = node.prev;
            }
            tasks.add(node.task); // добавили первый элемент в список

        }
        return tasks;
    }

    protected void removeNode(Node node) {
            if (node.next == null) {
                if (node.prev != null) {
                    node.prev.next = null;
                }
                last = node.prev;
            }
            if (node.prev == null) {
                if (node.next != null) {
                    node.next.prev = null;
                }
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

    @Override
    public void remove(int id) {
        Node node = map.get(id);
        if (node != null) {
            removeNode(node);
            map.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

}
