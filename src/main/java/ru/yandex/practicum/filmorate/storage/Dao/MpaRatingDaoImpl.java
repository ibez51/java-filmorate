package ru.yandex.practicum.filmorate.storage.Dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Qualifier("dbStorage")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MpaRatingDaoImpl implements MpaRatingDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> getRatingById(Integer id) {
        String sql = "SELECT id, name FROM mpa_rating " +
                "WHERE id = ? " +
                "LIMIT 1;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpaRating(rs), id);
    }

    @Override
    public List<MpaRating> getAllRatings() {
        String sql = "SELECT id, name FROM mpa_rating " +
                "ORDER BY id ASC;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpaRating(rs));
    }

    private MpaRating makeMpaRating(ResultSet rs) throws SQLException {
        return MpaRating.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }
}
