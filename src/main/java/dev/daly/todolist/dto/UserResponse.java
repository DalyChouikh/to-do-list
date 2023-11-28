package dev.daly.todolist.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserResponse {
    private String username;
    private List<TaskResponse> tasks;
}
