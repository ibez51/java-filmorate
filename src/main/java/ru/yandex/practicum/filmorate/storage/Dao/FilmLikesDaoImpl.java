package ru.yandex.practicum.filmorate.storage.Dao;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.storage.FilmLikesDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Qualifier("dbStorage")
@AllArgsConstructor
public class FilmLikesDaoImpl implements FilmLikesDao {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmLikes> getTopPopularFilms(Integer filmsCount) {
        String sql = "SELECT id AS film_id, count(user_id) AS user_id FROM film " +
                "LEFT OUTER JOIN film_likes_user_link " +
                "ON film_id = id " +
                "GROUP BY id " +
                "ORDER BY user_id DESC, id ASC " +
                "LIMIT ?;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmLikes(rs), filmsCount);
    }

    @Override
    public boolean isLikeExists(Integer filmId, Integer userId) {
        String sql = "SELECT 'x' FROM film_likes_user_link " +
                "WHERE film_id = ? " +
                "AND user_id = ? " +
                "LIMIT 1;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, filmId, userId);

        return rowSet.next();
    }


    @Override
    public void likeFilm(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes_user_link " +
                "(film_id, user_id) " +
                "VALUES (?, ?);";

        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void dislikeFilm(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes_user_link " +
                "WHERE film_id = ? " +
                "AND user_id = ?;";

        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<FilmLikes> findLikesListByFilm(Integer filmId) {
        String sql = "SELECT film_id, user_id FROM film_likes_user_link " +
                "WHERE film_id = ? " +
                "ORDER BY user_id ASC;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilmLikes(rs), filmId);
    }

    private FilmLikes makeFilmLikes(ResultSet rs) throws SQLException {
        return FilmLikes.builder()
                .filmId(rs.getInt("film_id"))
                .userId(rs.getInt("user_id"))
                .build();
    }
}
