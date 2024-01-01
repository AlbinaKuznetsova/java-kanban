package tasks;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected Status status; // NEW, IN_PROGRESS, DONE
    protected TaskType type; //
    protected Integer duration;
    protected LocalDateTime startTime;
    private static int count = 0;
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = generateId();
        this.status = Status.NEW;
        this.type = TaskType.TASK;
        this.duration = 0;
    }

    public Task(String name, String description, Integer duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = generateId();
        this.status = Status.NEW;
        this.type = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, int id, Status status, Integer duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
        if (id > count) {
            setCount(id);
        }
    }
    public Task(String name, String description, int id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type = TaskType.TASK;
        this.duration = 0;
        if (id > count) {
            setCount(id);
        }
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                ", startTime='" + startTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(description, task.description)
                && Objects.equals(id, task.id) && Objects.equals(status, task.status)
                && Objects.equals(startTime, task.startTime) && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    private void setCount(Integer id) {
        count = id;
    }
    public LocalDateTime getEndTime() {
        LocalDateTime endTime = null;
        try {
            endTime = startTime.plusMinutes(duration);
        } catch (NullPointerException ex) {

        }
        return endTime;
    }
    public Integer getDuration() {
        return duration;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }
    Integer generateId() {
        return ++count;
    }

}
