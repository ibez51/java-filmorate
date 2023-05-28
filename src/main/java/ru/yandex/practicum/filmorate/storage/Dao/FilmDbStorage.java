package ru.yandex.practicum.filmorate.storage.Dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@Qualifier("dbStorage")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FilmDbStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT id, name, description, release_date, duration, mpa_rating_id FROM film ORDER BY id ASC;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film findFilm(Integer filmId) {
        String sql = "SELECT id, name, description, release_date, duration, mpa_rating_id FROM film " +
                "WHERE id = ? " +
                "LIMIT 1;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), filmId).stream().findFirst().orElseGet(null);
    }

    @Override
    public Integer addFilm(Film film) {
        SimpleJdbcInsert insertIntoFilm = new SimpleJdbcInsert(jdbcTemplate).withTableName("film").usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_rating_id", film.getMpa().getId());

        return insertIntoFilm.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public void updateFilm(Film film) {
        String sql = "UPDATE film " +
                "SET name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_rating_id = ? " +
                "WHERE id = ?;";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    @Override
    public Film likeFilm(Integer filmId, Integer userId) {
        return null;
    }

    @Override
    public Film dislikeFilm(Integer filmId, Integer userId) {
        return null;
    }

    @Override
    public Set<Film> getTopPopularFilms(Integer filmsCount) {
        return null;
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
                .mpa(MpaRating.builder().id(rs.getInt("mpa_rating_id")).build())
                .build();
    }
}
