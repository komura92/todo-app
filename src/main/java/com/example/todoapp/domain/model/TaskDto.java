package com.example.todoapp.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record TaskDto(Long id,
                      String name,
                      String description,
                      int priority,
                      LocalDateTime deadline,
                      TaskStatus status) {
}
