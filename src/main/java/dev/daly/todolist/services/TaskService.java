package dev.daly.todolist.services;

import dev.daly.todolist.dto.TaskRequest;
import dev.daly.todolist.dto.TaskResponse;
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
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;

    public ResponseEntity<String> createTask(TaskRequest taskRequest){
        ResponseEntity<String> validationResponse = validateTaskRequest(taskRequest);
        if(validationResponse != null){
            return validationResponse;
        } else if(taskRepository.existsByTitle(taskRequest.getTitle())){
            return new ResponseEntity<>("Task with title " + taskRequest.getTitle() + " already exists", HttpStatus.BAD_REQUEST);
        }
        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .status(taskRequest.getStatus())
                .dueDate(LocalDate.parse(taskRequest.getDueDate()))
                .build();
        taskRepository.save(task);
        return new ResponseEntity<>("Task created successfully", HttpStatus.CREATED);
    }
    public ResponseEntity<?> getTaskById(Long id){
        Optional<Task> task = taskRepository.findById(id);
        if(task.isEmpty()){
            return new ResponseEntity<>("Task with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }
        TaskResponse taskResponse = buildTaskResponse(task.get());
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    private static TaskResponse buildTaskResponse(Task task) {
        return TaskResponse.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

    public ResponseEntity<?> getTasksByTitleAndStatus(String keyword, String status) {
        List<TaskResponse> tasks;
        if(status.isEmpty()){
             tasks = taskRepository.findTasksByTitleContains(keyword).stream().map(TaskService::buildTaskResponse).toList();
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        }
        Status statusEnum;
        if(status.toLowerCase().contains("progress")){
            statusEnum = Status.IN_PROGRESS;
        }else if(status.toLowerCase().contains("done")) {
            statusEnum = Status.DONE;
        }else{
            return new ResponseEntity<>("Invalid status. Status must be either In Progress or Done", HttpStatus.BAD_REQUEST);
        }
        tasks = taskRepository.findTasksByTitleContainsAndStatus(keyword, statusEnum).stream().map(TaskService::buildTaskResponse).toList();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    public ResponseEntity<String> updateTask(Long taskId, TaskRequest taskRequest) {
        Optional<Task> task = taskRepository.findById(taskId);
        if(task.isEmpty()){
            return new ResponseEntity<>("Task with ID " + taskId + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        ResponseEntity<String> validationResponse = validateTaskRequest(taskRequest);
        if(validationResponse != null){
            return validationResponse;
        }else if(taskRepository.existsByTitle(taskRequest.getTitle()) && !task.get().getTitle().equals(taskRequest.getTitle())){
            return new ResponseEntity<>("Task with title " + taskRequest.getTitle() + " already exists", HttpStatus.BAD_REQUEST);
        }
        task.get().setTitle(taskRequest.getTitle());
        task.get().setDescription(taskRequest.getDescription());
        task.get().setStatus(taskRequest.getStatus());
        task.get().setDueDate(LocalDate.parse(taskRequest.getDueDate()));
        taskRepository.save(task.get());
        return new ResponseEntity<>("Task with ID " + taskId + " updated successfully", HttpStatus.OK);
    }
    public ResponseEntity<String> deleteTask(Long taskId) {
        if(!taskRepository.existsById(taskId)){
            return new ResponseEntity<>("Task with ID " + taskId + " doesn't exist", HttpStatus.NOT_FOUND);
        }
        taskRepository.deleteById(taskId);
        return new ResponseEntity<>("Task with ID " + taskId + " deleted successfully", HttpStatus.OK);
    }
    private ResponseEntity<String> validateTaskRequest(TaskRequest taskRequest) {
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(taskRequest.getDueDate());
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format. Expected format: yy-MM-dd", HttpStatus.BAD_REQUEST);
        }
        if(taskRequest.getTitle() == null || taskRequest.getTitle().isEmpty()){
            return new ResponseEntity<>("Title cannot be empty", HttpStatus.BAD_REQUEST);
        }
        else if(taskRequest.getDueDate() == null || taskRequest.getDueDate().isEmpty()){
            return new ResponseEntity<>("Due date cannot be empty", HttpStatus.BAD_REQUEST);
        }
        else if(taskRequest.getStatus() == null){
            return new ResponseEntity<>("Status cannot be empty", HttpStatus.BAD_REQUEST);
        } else if (taskRequest.getStatus() != Status.IN_PROGRESS && taskRequest.getStatus() != Status.DONE) {
            return new ResponseEntity<>("Status must be either In Progress or Done", HttpStatus.BAD_REQUEST);
        } else if(parsedDate.isBefore(LocalDate.now())){
            return new ResponseEntity<>("Due date cannot be in the past", HttpStatus.BAD_REQUEST);
        }
        else if (taskRequest.getStatus() == Status.DONE && parsedDate.isAfter(LocalDate.now())){
            return new ResponseEntity<>("Due date cannot be in the future if status is done", HttpStatus.BAD_REQUEST);
        }
        else if (taskRequest.getStatus() == Status.IN_PROGRESS && parsedDate.isBefore(LocalDate.now())){
            return new ResponseEntity<>("Due date cannot be in the past if status is in progress", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}
