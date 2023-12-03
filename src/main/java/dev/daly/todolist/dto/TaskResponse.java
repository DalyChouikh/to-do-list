package dev.daly.todolist.dto;

import dev.daly.todolist.models.Status;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    @NonNull
    private String title;
    @Nullable
    private Status status;
}
