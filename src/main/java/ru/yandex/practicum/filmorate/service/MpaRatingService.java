package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingDao;

import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingDao mpaRatingDao;

    public MpaRatingService(MpaRatingDao mpaRatingDao) {
        this.mpaRatingDao = mpaRatingDao;
    }

    public MpaRating getRatingById(Integer id) {
        return mpaRatingDao.getRatingById(id);
    }

    public List<MpaRating> getAllRatings() {
        return mpaRatingDao.getAllRatings();
    }
}
