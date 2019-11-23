package edu.uwm.capstone.db;

import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class DegreeProgramDao extends BaseDao<Long, DegreeProgram> {

    @Autowired
    private DegreeProgramDao degreeProgramDao;

    private static final Logger LOG = LoggerFactory.getLogger(DegreeProgramDao.class);

    @Override
    public DegreeProgram create(DegreeProgram dp) {
        if(dp == null) {
            throw new DaoException("Request to create a new degree program received null.");
        }
        else if (dp.getId() != null) {
            throw new DaoException("When creating a new degree program the id should be null, but was set to " + dp.getId());
        }

        LOG.trace("Creating degree program {}", dp);

        dp.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbcTemplate.update(sql("createDegreeProgram"),
                new MapSqlParameterSource(rowMapper.mapObject(dp)),
                keyHolder,
                new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create degree program %s - affected %s rows.",
                    dp.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        dp.setId(id);
        return dp;
    }

    @Override
    public DegreeProgram read(Long id) {
        LOG.trace("Reading degree program {}", id);
        try {
            return (DegreeProgram) jdbcTemplate.queryForObject(sql("readDegreeProgramById"),
                    new MapSqlParameterSource("id", id), rowMapper);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public DegreeProgram read(String name) {
        LOG.trace("Reading degree program {}", name);
        try {
            return (DegreeProgram) jdbcTemplate.queryForObject(sql("readDegreeProgramByName"),
                    new MapSqlParameterSource("name", name), rowMapper);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<DegreeProgram> readAll() {
        LOG.trace("Reading all degree programs");
        List<DegreeProgram> degreePrograms = jdbcTemplate.query(sql("readAllDegreePrograms"), rowMapper);
        return degreePrograms;
    }

    @Override
    public DegreeProgram update(DegreeProgram dp) {
        if(dp == null) {
            throw new DaoException("Request to update a degree program received null.");
        }
        else if (dp.getId() != null) {
            throw new DaoException("When updating a degree program the id should not be null");
        }

        LOG.trace("Updating degree program {}", dp);

        dp.setUpdatedDate(LocalDateTime.now());
        int result = jdbcTemplate.update(sql("updateDegreeProgramById"),
                new MapSqlParameterSource(rowMapper.mapObject(dp)));
        if(result != 1) {
            throw new DaoException(String.format("Failed attempt to update degree program %s - %s rows affected.",
                    dp.toString(), result));
        }
        return dp;
    }

    @Override
    public void delete(Long id) {
        LOG.trace("Deleting degree program {}", id);
        int result = jdbcTemplate.update(sql("deleteDegreeProgramById"),
                new MapSqlParameterSource("id", id));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete degree program %s - %s rows affected.",
                    id, result));
        }
    }
}