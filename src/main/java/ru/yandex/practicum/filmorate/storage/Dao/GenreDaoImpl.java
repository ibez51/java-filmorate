package ru.yandex.practicum.filmorate.storage.Dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findGenresByFilmId(Integer filmId) {
        String sql = "SELECT genre.id, genre.name FROM film_genre AS film_genre " +
                "INNER JOIN genre AS genre " +
                "ON genre.id = film_genre.genre_id " +
                "WHERE film_genre.film_id = ?;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId).stream().collect(Collectors.toList());
    }

    @Override
    public Genre findGenreById(Integer id) {
        String sql = "SELECT id, name FROM genre " +
                "WHERE id = ? " +
                "LIMIT 1;";

        List<Genre> resultList = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);

        if (resultList.size() == 1) {
            Genre genre = resultList.get(0);

            log.info("Genre found: id = {} name = {}", genre.getId(), genre.getName());

            return genre;
        } else {
            log.info("Genre with id = {} not found", id);

            throw new NullPointerException("Жанр с Id = " + id + " не найден.");
        }
    }

    public List<Genre> getAllGenres() {
        String sql = "SELECT id, name FROM genre " +
                "ORDER BY id ASC;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    public void addFilmGenre(Integer filmId, Integer genreId) {
        String sql = "SELECT 'x' FROM film_genre " +
                "WHERE film_id = ? " +
                "AND genre_id = ? " +
                "LIMIT 1;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, filmId, genreId);

        if (!rowSet.next()) {
            sql = "INSERT INTO film_genre " +
                    "(film_id, genre_id) " +
                    "VALUES (?, ?);";

            jdbcTemplate.update(sql, filmId, genreId);
        }
    }

    public void deleteAllFilmGenres(Integer filmId) {
        String sql = "DELETE FROM film_genre " +
                "WHERE film_id = ?;";

        jdbcTemplate.update(sql, filmId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
