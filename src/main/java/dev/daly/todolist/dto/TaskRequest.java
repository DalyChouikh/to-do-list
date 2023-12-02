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
    @NonNull
    private Status status;
}
