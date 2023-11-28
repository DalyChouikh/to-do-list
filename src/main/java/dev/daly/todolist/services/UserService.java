package dev.daly.todolist.services;


import dev.daly.todolist.dto.TaskResponse;
import dev.daly.todolist.dto.UserRequest;
import dev.daly.todolist.dto.UserResponse;
import dev.daly.todolist.models.Task;
import dev.daly.todolist.models.User;
import dev.daly.todolist.repository.TaskRepository;
import dev.daly.todolist.repository.UserRepository;
import dev.daly.todolist.security.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(hashPassword(userRequest.getPassword()))
                .build();
        userRepository.save(user);
        return ResponseEntity.ok().body("User created successfully");
    }


    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public ResponseEntity<?> getAllUsers() {
        List<UserResponse> userResponses = userRepository.findAll().stream()
                .map(this::mapUserToUserResponse).toList();
        return ResponseEntity.ok().body(userResponses);
    }

    private UserResponse mapUserToUserResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .tasks(user.getTasks().stream().map(this::mapTaskToTaskResponse).toList())
                .build();
    }
    private TaskResponse mapTaskToTaskResponse(Task task) {
        return TaskResponse.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build();
    }
}
