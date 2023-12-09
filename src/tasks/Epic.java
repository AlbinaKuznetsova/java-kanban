package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId; // Список id подзадач эпика

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status) {
        super(name, description, id, status);
        subtasksId = new ArrayList<>();
        this.type = TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "id=" + id +
                ", subtasksId=" + subtasksId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksId);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }
}
