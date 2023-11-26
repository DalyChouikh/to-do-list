package dev.daly.todolist.Controller;

import dev.daly.todolist.dto.TaskRequest;
import dev.daly.todolist.dto.TaskResponse;
import dev.daly.todolist.models.Status;
import dev.daly.todolist.models.Task;
import dev.daly.todolist.services.TaskService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@AllArgsConstructor
@CrossOrigin
public class TaskController {
    private TaskService taskService;
    
    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@RequestParam(defaultValue = "") String keyword){
        return new ResponseEntity<>(taskService.getTasksByTitle(keyword), HttpStatus.OK);
    }

    @GetMapping("/{task_id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long task_id){

        return new ResponseEntity<>(taskService.getTaskById(task_id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@RequestBody TaskRequest taskRequest){
        return taskService.createTask(taskRequest);
    }

    @PutMapping("/update/{task_id}")
    public ResponseEntity<String> updateTask(@PathVariable Long task_id, @RequestBody TaskRequest taskRequest){
        return taskService.updateTask(task_id, taskRequest);
    }

    @DeleteMapping("/delete/{task_id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long task_id){
        return taskService.deleteTask(task_id);
    }




}
