package dev.daly.todolist.Controller;

import dev.daly.todolist.dto.TaskRequest;
import dev.daly.todolist.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("api/v1/tasks")
@AllArgsConstructor
@CrossOrigin
public class TaskController {
    private TaskService taskService;
    
    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "") String status){
        return taskService.getTasksByTitleAndStatus(keyword, status);
    }
    @GetMapping("/id/{task_id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long task_id){
        return taskService.getTaskById(task_id);
    }
    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskRequest taskRequest){
        return taskService.createTask(taskRequest);
    }

    @PutMapping("{task_id}")
    public ResponseEntity<String> updateTask(@PathVariable Long task_id, @RequestBody TaskRequest taskRequest){
        return taskService.updateTask(task_id, taskRequest);
    }

    @DeleteMapping("{task_id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long task_id){
        return taskService.deleteTask(task_id);
    }




}
