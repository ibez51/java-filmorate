package ru.yandex.practicum.filmorate.storage.Dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmLikesService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaRatingService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("dbStorage")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;
    private MpaRatingService mpaRatingService;
    private GenreService genreService;
    private FilmLikesService filmLikesService;
    private UserService userService;

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT id, name, description, release_date, duration, mpa_rating_id FROM film;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findFilm(Integer filmId) {
        Film film;
        String sql = "SELECT id, name, description, release_date, duration, mpa_rating_id FROM film " +
                "WHERE id = ? " +
                "LIMIT 1;";
        List<Film> resultList = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmId);

        if (resultList.size() == 1) {
            film = resultList.get(0);

            log.info("Film found: id = {} name = {}", film.getId(), film.getName());

            return film;
        } else {
            log.info("Film with id = {} not found", filmId);

            throw new NullPointerException("Фильм с Id = " + filmId + " не найден.");
        }
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert insertIntoFilm = new SimpleJdbcInsert(jdbcTemplate).withTableName("film").usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_rating_id", film.getMpa().getId());

        Integer filmId = insertIntoFilm.executeAndReturnKey(parameters).intValue();

        if (!Objects.isNull(film.getGenres())
                && !film.getGenres().isEmpty()) {
            for (Integer genreId : film.getGenres().stream().map(Genre::getId).collect(Collectors.toList())) {
                genreService.addFilmGenre(filmId, genreId);
            }
        }

        log.info("Created film with id {}", filmId);

        return findFilm(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE film " +
                "SET name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_rating_id = ? " +
                "WHERE id = ?;";

        findFilm(film.getId());//check that film exists

        int rowsAffected = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (rowsAffected == 0) {
            throw new RuntimeException("Film with id = " + film.getId() + " wasn't updated");
        }

        genreService.deleteAllFilmGenres(film.getId());

        if (!Objects.isNull(film.getGenres())
                && !film.getGenres().isEmpty()) {
            for (Integer genreId : film.getGenres().stream().map(Genre::getId).collect(Collectors.toList())) {
                genreService.addFilmGenre(film.getId(), genreId);
            }
        }

        log.info("Film with id = {} was updated", film.getId());

        return findFilm(film.getId());
    }

    @Override
    public Film likeFilm(Integer filmId, Integer userId) {
        findFilm(filmId);
        userService.findUser(userId);

        filmLikesService.likeFilm(filmId, userId);

        log.info("Film with id {} liked", filmId);

        return findFilm(filmId);
    }

    @Override
    public Film dislikeFilm(Integer filmId, Integer userId) {
        findFilm(filmId);
        userService.findUser(userId);

        filmLikesService.dislikeFilm(filmId, userId);

        log.info("Film with id {} disliked", filmId);

        return findFilm(filmId);
    }

    @Override
    public Set<Film> getTopPopularFilms(Integer filmsCount) {
        return filmLikesService.getTopPopularFilms(filmsCount).stream()
                .map(x -> findFilm(x.getFilmId()))
                .collect(Collectors.toSet());
    }

    @Override
    public void resetIdNumberSeq() {

    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpaRatingService.getRatingById(rs.getInt("mpa_rating_id")))
                .genres(genreService.findGenresByFilmId(rs.getInt("id")))
                .userLikesSet(filmLikesService.findLikesListByFilm(rs.getInt("id")).stream()
                        .map(FilmLikes::getUserId).collect(Collectors.toSet()))
                .build();
    }
}
