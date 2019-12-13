package edu.uwm.capstone.db;

import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.model.DegreeProgramState;
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
import java.util.HashSet;
import java.util.List;

public class DegreeProgramDao extends BaseDao<Long, DegreeProgram> {

    @Autowired
    private DegreeProgramStateDao degreeProgramStateDao;

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

        // Create degree program states
        for(DegreeProgramState dps: dp) {
            dps.setDegreeProgramId(dp.getId());
            degreeProgramStateDao.create(dps);
        }
        return dp;
    }

    @Override
    public DegreeProgram read(Long id) {
        LOG.trace("Reading degree program {}", id);
        try {
            DegreeProgram dp =  (DegreeProgram) jdbcTemplate.queryForObject(sql("readDegreeProgramById"),
                    new MapSqlParameterSource("id", id), rowMapper);
            dp.setDegreeProgramStates(degreeProgramStateDao.readAllStatesByDegreeProgramId(dp.getId()));
            return dp;
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public DegreeProgram readByName(String name) {
        LOG.trace("Reading degree program {}", name);
        try {
            DegreeProgram dp = (DegreeProgram) jdbcTemplate.queryForObject(sql("readDegreeProgramByName"),
                    new MapSqlParameterSource("name", name), rowMapper);
            dp.setDegreeProgramStates(degreeProgramStateDao.readAllStatesByDegreeProgramId(dp.getId()));
            return dp;
        } catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<DegreeProgram> readAll() {
        LOG.trace("Reading all degree programs");
        List<DegreeProgram> degreePrograms = jdbcTemplate.query(sql("readAllDegreePrograms"), rowMapper);
        for(DegreeProgram dp: degreePrograms) {
            dp.setDegreeProgramStates(degreeProgramStateDao.readAllStatesByDegreeProgramId(dp.getId()));
        }
        return degreePrograms;
    }

    @Override
    public DegreeProgram update(DegreeProgram dp) {
        if(dp == null) {
            throw new DaoException("Request to update a degree program received null.");
        }
        else if (dp.getId() == null) {
            throw new DaoException("When updating a degree program the id should be null.");
        }

        LOG.trace("Updating degree program {}", dp);

        dp.setUpdatedDate(LocalDateTime.now());
        int result = jdbcTemplate.update(sql("updateDegreeProgramById"),
                new MapSqlParameterSource(rowMapper.mapObject(dp)));
        if(result != 1) {
            throw new DaoException(String.format("Failed attempt to update degree program %s - %s rows affected.",
                    dp.toString(), result));
        }

        HashSet<Long> fieldDefIdsAssociatedWithOldFormDef = new HashSet<>(degreeProgramStateDao.readAllStatesIdsByDegreeProgramId(dp.getId()));

        Long fieldDefId;
        for (DegreeProgramState dps : dp) {
            fieldDefId = dps.getId();
            if (fieldDefId == null) {                                              // create new field def if has null id
                dps.setDegreeProgramId(dp.getId());
                degreeProgramStateDao.create(dps);
            } else if (fieldDefIdsAssociatedWithOldFormDef.contains(fieldDefId)) { // update field def if it's connected to formDef
                fieldDefIdsAssociatedWithOldFormDef.remove(fieldDefId);
                degreeProgramStateDao.update(dps);
            }
        }

        fieldDefIdsAssociatedWithOldFormDef.forEach(fdId -> degreeProgramStateDao.delete(fdId)); // remove old field defs
        return dp;
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new DaoException("When deleting a degree program the id should be null");
        }
        LOG.trace("Deleting degree program {}", id);

        List<DegreeProgramState> dpStates = degreeProgramStateDao.readAllStatesByDegreeProgramId(id);
        for (DegreeProgramState degreeProgramState : dpStates) {
            degreeProgramStateDao.delete(degreeProgramState.getId());
        }

        int result = jdbcTemplate.update(sql("deleteDegreeProgramById"),
                new MapSqlParameterSource("id", id));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete degree program %s - %s rows affected.",
                    id, result));
        }
    }
}
