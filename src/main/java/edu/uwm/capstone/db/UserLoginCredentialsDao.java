package edu.uwm.capstone.db;

import edu.uwm.capstone.model.UserLoginCredentials;
import edu.uwm.capstone.sql.dao.BaseDao;
import edu.uwm.capstone.sql.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class UserLoginCredentialsDao extends BaseDao<String, UserLoginCredentials> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginCredentialsDao.class);

    @Override
    public UserLoginCredentials create(UserLoginCredentials object) {
        // this is intentionally left blank
        return null;
    }

    @Override
    public UserLoginCredentials read(String email) throws DaoException {
        if (email == null) {
            throw new DaoException("Request to read a UserLoginCredentials received null");
        }

        LOGGER.trace("Reading UserLoginCredentials email: {}", email);

        try {
            return (UserLoginCredentials) this.jdbcTemplate.queryForObject(sql("readProfileByEmail"), new MapSqlParameterSource("email", email), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean update(UserLoginCredentials object) {
        // this is intentionally left blank
        return false;
    }

    @Override
    public boolean delete(String id) {
        // this is intentionally left blank
        return false;
    }

}
