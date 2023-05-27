package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friendship {
    Integer userId;
    Integer friendId;
    Integer friendshipStatus;
}
