package dev.daly.todolist.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Status {
    TODO("To Do"),
    DONE("Done");

    private final String status;

    Status(String s) {
        this.status = s;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Status fromStatus(String value) {
        for (Status status : Status.values()) {
            if (status.status.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + value + ", Allowed values are " + allowedValues());
    }

    private static String allowedValues() {
        StringBuilder allowedValues = new StringBuilder();
        for (Status status : Status.values()) {
            allowedValues.append(status.status).append(", ");
        }
        return allowedValues.substring(0, allowedValues.length() - 2);
    }


}
