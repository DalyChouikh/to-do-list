package dev.daly.todolist.services;


import dev.daly.todolist.dto.TaskRequest;
import dev.daly.todolist.dto.TaskResponse;
import dev.daly.todolist.dto.UserRequest;
import dev.daly.todolist.dto.UserResponse;
import dev.daly.todolist.models.Status;
import dev.daly.todolist.models.Task;
import dev.daly.todolist.models.User;
import dev.daly.todolist.repository.TaskRepository;
import dev.daly.todolist.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;
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

    public ResponseEntity<?> getUser(String username) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            return ResponseEntity.ok().body(mapUserToUserResponse(user));
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
    }


    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public ResponseEntity<?> getAllUsers() {
        List<UserResponse> userResponses = userRepository.findAll().stream()
                .map(this::mapUserToUserResponse).toList();
        return ResponseEntity.ok().body(userResponses);
    }

    public ResponseEntity<?> getUserTasks(String username) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            List<TaskResponse> taskResponses = user.getTasks().stream()
                    .map(this::mapTaskToTaskResponse).toList();
            return ResponseEntity.ok().body(taskResponses);
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
    }

    public ResponseEntity<?> deleteUser(String username) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            userRepository.delete(user);
            return ResponseEntity.ok().body("User " + username + " deleted successfully");
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
    }

    public ResponseEntity<?> updateUser(String username, UserRequest userRequest) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            if (userRequest.getUsername() != null && !userRequest.getUsername().equals(username)) {
                if (userRepository.existsByUsername(userRequest.getUsername())) {
                    return ResponseEntity.badRequest().body("Username already exists");
                }
            }
            User newUser = User.builder()
                    .username(userRequest.getUsername())
                    .password(user.getPassword())
                    .tasks(user.getTasks())
                    .build();
            userRepository.delete(user);
            if(userRequest.getPassword() != null){
                user.setPassword(hashPassword(userRequest.getPassword()));
            }
            userRepository.save(newUser);
            return ResponseEntity.ok().body("User updated successfully");
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
    }

    public ResponseEntity<?> getUserTask(String username, String title) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            Optional<Task> task = user.getTasks().stream().filter(t -> t.getTitle().equals(title)).findFirst();
            if (task.isPresent()) {
                return ResponseEntity.ok().body(mapTaskToTaskResponse(task.get()));
            }
            return ResponseEntity.ok().body("Task " + title + " does not exist");
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
    }
    public ResponseEntity<?> createUserTask(String username, TaskRequest taskRequest) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            ResponseEntity<String> validationResponse = TaskService.validateTaskRequestInPost(taskRequest);
            if (validationResponse != null) {
                return validationResponse;
            }
            Optional<Task> task = user.getTasks().stream().filter(t -> t.getTitle().equals(taskRequest.getTitle())).findFirst();
            if (task.isPresent()) {
                return ResponseEntity.badRequest().body("Task " + taskRequest.getTitle() + " already exists");
            }
            Task newTask = Task.builder()
                    .title(taskRequest.getTitle())
                    .status(Status.IN_PROGRESS)
                    .build();
            taskRepository.save(newTask);
            user.getTasks().add(newTask);
            userRepository.save(user);
            return ResponseEntity.ok().body("Task " + taskRequest.getTitle() + " created successfully");
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
    }

    public ResponseEntity<?> deleteUserTask(String username, String title) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            Optional<Task> task = user.getTasks().stream().filter(t -> t.getTitle().equals(title)).findFirst();
            if (task.isPresent()) {
                user.getTasks().remove(task.get());
                taskRepository.deleteById(task.get().getId());
                userRepository.save(user);
                return ResponseEntity.ok().body("Task " + task.get().getTitle() + " deleted successfully");
            }
            return ResponseEntity.ok().body("Task " + title  + " does not exist");
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
    }

    public ResponseEntity<?> updateUserTask(String username, String title, TaskRequest taskRequest) {
        if (userRepository.existsByUsername(username)) {
            User user = userRepository.findByUsername(username);
            Optional<Task> task = user.getTasks().stream().filter(t -> t.getTitle().equals(title)).findFirst();
            if (task.isPresent()) {
                ResponseEntity<String> validationResponse = TaskService.validateTaskRequestInPut(taskRequest);
                if (validationResponse != null) {
                    return validationResponse;
                } else if (user.getTasks().stream().anyMatch(t -> t.getTitle().equalsIgnoreCase(taskRequest.getTitle())) && !task.get().getTitle().equals(taskRequest.getTitle())) {
                    return ResponseEntity.badRequest().body("Task with title " + taskRequest.getTitle() + " already exists");
                }
                task.get().setTitle(taskRequest.getTitle());
                task.get().setStatus(taskRequest.getStatus());
                taskRepository.save(task.get());
                return ResponseEntity.ok().body("Task " + taskRequest.getTitle() + " updated successfully");
            }
            return ResponseEntity.ok().body("Task " + title + " does not exist");
        }
        return ResponseEntity.ok().body("User " + username + " does not exist");
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
                .status(task.getStatus())
                .build();
    }
}
