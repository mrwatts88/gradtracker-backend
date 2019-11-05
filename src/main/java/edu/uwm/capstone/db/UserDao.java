package edu.uwm.capstone.db;

import edu.uwm.capstone.model.User;
import edu.uwm.capstone.security.Authorities;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class UserDao extends BaseDao<Long, User> {

    private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);

    /**
     * Create a {@link User} object.
     *
     * @param user {@link User}
     * @return {@link User}
     */
    @Override
    public User create(User user) {
        if (user == null) {
            throw new DaoException("Request to create a new user received null");
        } else if (user.getId() != null) {
            throw new DaoException("When creating a new user the id should be null, but was set to " + user.getId());
        }

        LOG.trace("Creating user {}", user);

        user.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createUser"),
                new MapSqlParameterSource(rowMapper.mapObject(user)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create user %s - affected %s rows", user.toString(), result));
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);

        // TODO create entries in user_roles table for each role the user has (use jdbc.batchUpdate)

        return user;
    }

    /**
     * Retrieve a {@link User} object by its Id.
     *
     * @param userId
     * @return {@link User}
     */
    @Override
    public User read(Long userId) {
        LOG.trace("Reading user {}", userId);
        try {
            User user = (User) this.jdbcTemplate.queryForObject(sql("readUser"), new MapSqlParameterSource("id", userId), rowMapper);

            setUserRolesAndAuthorities(user);
            return user;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Returns a list of all {@link User} objects from the database.
     *
     * @return
     */
    public List<User> readAll() {
        LOG.trace("Reading all users");
        List<User> users = this.jdbcTemplate.query(sql("readAllUsers"), rowMapper);

        for (User user : users) {
            setUserRolesAndAuthorities(user);
        }

        return users;
    }

    /**
     * Retrieve a {@link User} object by its email.
     *
     * @param email
     * @return {@link User}
     */
    public User readByEmail(String email) {
        LOG.trace("Reading user with email {}", email);
        try {
            User user = (User) this.jdbcTemplate.queryForObject(sql("readUserByEmail"), new MapSqlParameterSource("email", email), rowMapper);

            setUserRolesAndAuthorities(user);
            return user;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private void setUserRolesAndAuthorities(User user) {
        HashSet<String> roleNames = new HashSet<>();
        HashSet<Authorities> authorities = new HashSet<>();

        this.jdbcTemplate.query(sql("readRoleAuthoritiesByUserId"),
                new MapSqlParameterSource("user_id", user.getId()),
                (rs) -> {
                    // TODO may not want to hardcode these column names
                    roleNames.add(rs.getString("name"));
                    authorities.add(Authorities.valueOf(rs.getString("authority")));
                });
        user.setRoleNames(roleNames);
        user.setAuthorities(authorities);
    }

    /**
     * Update the provided {@link User} object.
     *
     * @param user {@link User}
     * @return true if successful
     */
    @Override
    public User update(User user) {
        if (user == null) {
            throw new DaoException("Request to update a new user received null");
        } else if (user.getId() == null) {
            throw new DaoException("When updating a new user the id should not be null");
        }

        LOG.trace("Updating user {}", user);
        user.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateUser"), new MapSqlParameterSource(rowMapper.mapObject(user)));

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update user %s - affected %s rows", user.toString(), result));
        }

        // TODO update entries in user_roles table for each role the user has

        return user;
    }

    /**
     * Delete a {@link User} object by its Id.
     *
     * @param userId
     * @return true if successful
     */
    @Override
    public void delete(Long userId) {
        LOG.trace("Deleting user {}", userId);
        this.jdbcTemplate.update(sql("deleteUserRolesByUserId"), new MapSqlParameterSource("user_id", userId));
        int result = this.jdbcTemplate.update(sql("deleteUser"), new MapSqlParameterSource("id", userId));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete user %s affected %s rows", userId, result));
        }
    }
}
