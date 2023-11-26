package dev.daly.todolist.dto;

import dev.daly.todolist.models.Status;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {
    @NonNull
    private String title;
    private String description;
    @NonNull
    private Status status;
    @NonNull
    private LocalDate dueDate;
}
