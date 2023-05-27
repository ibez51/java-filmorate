package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    @NotBlank
    @Email(message = "Некорректно указан email")
    private String email;
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
    @Builder.Default
    private Set<Integer> friendsSet = new HashSet<>();
}
