package com.dalycode.todolist.services;

import com.dalycode.todolist.models.Status;
import com.dalycode.todolist.models.Task;
import com.dalycode.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    public List<Task> getTasksByDescription(String keyword){
        return  taskRepository.findTasksByDescriptionContains(keyword);
    }

    public Task createTask(String description, Status status, Date dueDate){
        return  taskRepository.save(new Task(description, status, dueDate));
    }

    public Task createOrUpdateTask(Task task){
        return taskRepository.save(task);
    }
}
