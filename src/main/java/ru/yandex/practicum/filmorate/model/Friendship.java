package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendship {
    private Integer userId;
    private Integer friendId;
    private Integer friendshipStatus;
}
