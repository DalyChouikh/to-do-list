package dev.daly.todolist.repository;

import dev.daly.todolist.dto.TaskResponse;
import dev.daly.todolist.models.Status;
import dev.daly.todolist.models.Task;
import dev.daly.todolist.models.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByTitleContains(String keyword);
    boolean existsByTitle(String title);
    List<Task> findTasksByTitleContainsAndStatus(String keyword, Status status);
}
