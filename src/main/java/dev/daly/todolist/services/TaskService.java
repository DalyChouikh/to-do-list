package dev.daly.todolist.services;

import dev.daly.todolist.models.Status;
import dev.daly.todolist.models.Task;
import dev.daly.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " not found"));
    }

    public List<Task> getTasksByTitle(String keyword){
        return  taskRepository.findTasksByTitleContains(keyword);
    }

    public Task createTask(String title, String description, Status status, LocalDate dueDate){
        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .dueDate(dueDate)
                .build();
        return  taskRepository.save(task);
    }

    public Task createOrUpdateTask(Task task){
        return taskRepository.save(task);
    }
}
