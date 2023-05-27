package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма не может быть 0 или меньше")
    private Integer duration;
    @Builder.Default
    private MpaRating mpa = MpaRating.builder().id(1).name("").build();
    @Builder.Default
    private List<Genre> genres = new ArrayList<>();
    @Builder.Default
    private Set<Integer> userLikesSet = new HashSet<>();
}
