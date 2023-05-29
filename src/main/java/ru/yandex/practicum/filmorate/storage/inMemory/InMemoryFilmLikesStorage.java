package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.storage.FilmLikesDao;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Qualifier("inMemoryStorage")
public class InMemoryFilmLikesStorage implements FilmLikesDao {
    @Qualifier("inMemoryStorage")
    private final FilmStorage inMemotyFilmStorage;

    @Override
    public List<FilmLikes> getTopPopularFilms(Integer filmsCount) {
        return inMemotyFilmStorage.getTopPopularFilms(filmsCount).stream()
                .map(x -> FilmLikes.builder()
                        .filmId(x.getId())
                        .userId(x.getUserLikesSet().size())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isLikeExists(Integer filmId, Integer userId) {
        return inMemotyFilmStorage.findFilm(filmId).getUserLikesSet().contains(userId);
    }

    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        inMemotyFilmStorage.likeFilm(filmId, userId);
    }

    @Override
    public void dislikeFilm(Integer filmId, Integer userId) {
        inMemotyFilmStorage.dislikeFilm(filmId, userId);
    }

    @Override
    public List<FilmLikes> findLikesListByFilm(Integer filmId) {
        return inMemotyFilmStorage.findFilm(filmId).getUserLikesSet().stream()
                .map(x -> FilmLikes.builder()
                        .filmId(filmId)
                        .userId(x)
                        .build())
                .collect(Collectors.toList());
    }
}
