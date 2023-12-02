package dev.daly.todolist.Controller;

import dev.daly.todolist.dto.TaskRequest;
import dev.daly.todolist.dto.UserRequest;
import dev.daly.todolist.services.TaskService;
import dev.daly.todolist.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final TaskService taskService;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getUser(@PathVariable String username){
        return userService.getUser(username);
    }

    @GetMapping("{username}/tasks")
    public ResponseEntity<?> getUserTasks(@PathVariable String username){
        return userService.getUserTasks(username);
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username){
        return userService.deleteUser(username);
    }

    @PutMapping("{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody UserRequest userRequest){
        return userService.updateUser(username, userRequest);
    }

    @GetMapping("{username}/tasks/{title}")
    public ResponseEntity<?> getUserTask(@PathVariable String username, @PathVariable String title){
        return userService.getUserTask(username, title);
    }

    @DeleteMapping("{username}/tasks/{title}")
    public ResponseEntity<?> deleteUserTask(@PathVariable String username, @PathVariable String title){
        return userService.deleteUserTask(username, title);
    }

    @PutMapping("{username}/tasks/{title}")
    public ResponseEntity<?> updateUserTask(@PathVariable String username, @PathVariable String title, @RequestBody TaskRequest taskRequest){
        return userService.updateUserTask(username, title, taskRequest);
    }

    @PostMapping("{username}/tasks")
    public ResponseEntity<?> createUserTask(@PathVariable String username, @RequestBody TaskRequest taskRequest){
        return userService.createUserTask(username, taskRequest);
    }

}
