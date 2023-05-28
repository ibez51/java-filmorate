package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingDao;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MpaRatingService {
    @Qualifier("dbStorage")
    private final MpaRatingDao mpaRatingDao;

    public MpaRating getRatingById(Integer id) {
        MpaRating mpaRating = mpaRatingDao.getRatingById(id).stream().findFirst().orElse(null);

        if (Objects.isNull(mpaRating)) {
            throw new NullPointerException("Рейтинг с id = " + id + " не найден.");
        }

        return mpaRating;
    }

    public List<MpaRating> getAllRatings() {
        return mpaRatingDao.getAllRatings();
    }
}
