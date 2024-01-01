package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId; // Список id подзадач эпика
    LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description, int id, Status status, Integer duration, LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
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
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
