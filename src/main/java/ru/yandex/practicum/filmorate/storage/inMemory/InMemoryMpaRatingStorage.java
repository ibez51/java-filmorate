package ru.yandex.practicum.filmorate.storage.inMemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingDao;

import java.util.List;

@Component
@Qualifier("inMemoryStorage")
public class InMemoryMpaRatingStorage implements MpaRatingDao {
    @Override
    public List<MpaRating> getRatingById(Integer id) {
        return List.of(MpaRating.builder().build());
    }

    @Override
    public List<MpaRating> getAllRatings() {
        return null;
    }
}
