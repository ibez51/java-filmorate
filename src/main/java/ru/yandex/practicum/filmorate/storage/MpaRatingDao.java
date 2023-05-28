package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingDao {
    List<MpaRating> getRatingById(Integer id);

    List<MpaRating> getAllRatings();
}
