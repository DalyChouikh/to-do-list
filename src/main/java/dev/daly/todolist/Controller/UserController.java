package dev.daly.todolist.Controller;

import dev.daly.todolist.dto.UserRequest;
import dev.daly.todolist.services.TaskService;
import dev.daly.todolist.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TaskService taskService;


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

}
