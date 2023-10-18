import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    public ArrayList<Integer> subtasksId; // Список id подзадач эпика

    public Epic(String name, String description) {
        super(name, description);
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description, ArrayList<Integer> subtasksId) {
        super(name, description);
        this.subtasksId = subtasksId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksId=" + subtasksId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
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
}
