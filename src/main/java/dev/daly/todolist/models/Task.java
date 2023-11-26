package dev.daly.todolist.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "task")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(nullable = false)
    private String description;
    @NonNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @NonNull
    @Column(name = "due_date")
    private Date dueDate;
}
