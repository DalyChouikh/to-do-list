package dev.daly.todolist.Controller;

import dev.daly.todolist.models.Task;
import dev.daly.todolist.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@AllArgsConstructor
public class TaskController {
    private TaskService taskService;
    
    @GetMapping
    public ResponseEntity<List<Task>>getTasks(){
        return new ResponseEntity<>(taskService.getTasks(), HttpStatus.OK);
    }
}
