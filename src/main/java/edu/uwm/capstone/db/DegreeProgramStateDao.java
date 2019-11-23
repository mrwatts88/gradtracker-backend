package edu.uwm.capstone.db;

import edu.uwm.capstone.model.DegreeProgramState;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.dao.BaseRowMapper;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class DegreeProgramStateDao extends BaseDao<Long, DegreeProgramState> {

    private static final Logger LOG = LoggerFactory.getLogger(DegreeProgramStateDao.class);

    @Override
    public DegreeProgramState create(DegreeProgramState dpState) {
        if(dpState == null) {
            throw new DaoException("Request to create a new degree program state received null.");
        }
        else if(dpState.getId() != null) {
            throw new DaoException("When creating a new degree program state, the id should be null, but was set to " + dpState.getId());
        } else if (dpState.getDegreeProgramId() == null) {
            throw new DaoException("When creating a new degree program state, the degree program id should not be null.");
        }

        LOG.trace("Creating degree program state {}", dpState);

        dpState.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = jdbcTemplate.update(sql("createDegreeProgramState"),
                new MapSqlParameterSource(rowMapper.mapObject(dpState)),
                keyHolder,
                new String[]{BaseRowMapper.BaseColumnType.ID.name()});
        if(result != 1) {
            throw new DaoException(String.format("Failed attempt to create degree program state %s - %s rows affected.",
                    dpState.toString(), result));
        }

        Long id = keyHolder.getKey().longValue();
        dpState.setId(id);
        return dpState;
    }

    @Override
    public DegreeProgramState read(Long id) {
        LOG.trace("Reading degree program state {}", id);
        try {
            return (DegreeProgramState) jdbcTemplate.queryForObject(sql("readDegreeProgramStateById"),
                    new MapSqlParameterSource("id", id), rowMapper);
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<DegreeProgramState> readAll() {
        LOG.trace("Reading all degree program states.");
        List<DegreeProgramState> dpStates = jdbcTemplate.query(sql("readAllDegreeProgramStates"), rowMapper);
        return dpStates;
    }

    public List<DegreeProgramState> readAllStatesByDegreeProgramId(long degreeProgramId) {
        LOG.trace("Reading all degree program states by degree program id.");
        try {
            return this.jdbcTemplate.query(sql("readDegreeProgramStatesByDegreeProgramId"),
                    new MapSqlParameterSource("degree_program_id", degreeProgramId), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public DegreeProgramState update(DegreeProgramState dpState) {
        if(dpState == null) {
            throw new DaoException("Request to update a degree program state received null.");
        }
        else if (dpState.getId() == null) {
            throw new DaoException("When updating a degree program state, the id should not be null.");
        }

        LOG.trace("Updating a degree program state {}", dpState);

        dpState.setUpdatedDate(LocalDateTime.now());
        int result = jdbcTemplate.update(sql("updateDegreeProgramStateById"),
                new MapSqlParameterSource(rowMapper.mapObject(dpState)));
        if(result != 1) {
            throw new DaoException(String.format("Failed attempt to update degree program state %s - %s rows affected.",
                    dpState.toString(), result));
        }

        return dpState;
    }

    @Override
    public void delete(Long id) {

        LOG.trace("Deleting degree program state {}", id);
        int result = jdbcTemplate.update(sql("deleteDegreeProgramStateById"),
                new MapSqlParameterSource("id", id));
        if(result != 1) {
            throw new DaoException(String.format("Failed attempt to delete a degree program %s - %s rows affected.",
                    id, result));
        }
    }
}
