package dev.daly.todolist.services;

import dev.daly.todolist.dto.TaskRequest;
import dev.daly.todolist.models.Status;
import dev.daly.todolist.models.Task;
import dev.daly.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public ResponseEntity<String> createTask(TaskRequest taskRequest){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(taskRequest.getDueDate());
            if(taskRequest.getTitle() == null || taskRequest.getTitle().isEmpty()){
                return new ResponseEntity<>("Title cannot be empty", HttpStatus.BAD_REQUEST);
            }
            else if(parsedDate.isBefore(LocalDate.now())){
                return new ResponseEntity<>("Due date cannot be in the past", HttpStatus.BAD_REQUEST);
            }
            else if (taskRequest.getStatus() == Status.DONE && parsedDate.isAfter(LocalDate.now())){
                return new ResponseEntity<>("Due date cannot be in the future if status is done", HttpStatus.BAD_REQUEST);
            }
            else if (taskRequest.getStatus() == Status.IN_PROGRESS && parsedDate.isBefore(LocalDate.now())){
                return new ResponseEntity<>("Due date cannot be in the past if status is in progress", HttpStatus.BAD_REQUEST);
            }
            Task task = Task.builder()
                    .title(taskRequest.getTitle())
                    .description(taskRequest.getDescription())
                    .status(taskRequest.getStatus())
                    .dueDate(parsedDate)
                    .build();
            taskRepository.save(task);
            return new ResponseEntity<>("Task created successfully", HttpStatus.CREATED);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format. Expected format: yy-MM-dd", HttpStatus.BAD_REQUEST);
        }
    }

    public Task createOrUpdateTask(Task task){
        return taskRepository.save(task);
    }
    public ResponseEntity<String> deleteTask(Long taskId) {
        if(!taskRepository.existsById(taskId)){
            return new ResponseEntity<>("Task with ID " + taskId + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        taskRepository.deleteById(taskId);
        return new ResponseEntity<>("Task with ID " + taskId + " deleted successfully", HttpStatus.OK);
    }
}
