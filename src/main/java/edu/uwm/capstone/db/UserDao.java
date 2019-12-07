package edu.uwm.capstone.db;

import edu.uwm.capstone.model.User;
import edu.uwm.capstone.security.Authorities;
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
import java.util.Objects;
import java.util.Set;

public class UserDao extends BaseDao<Long, User> {

    private static final Logger LOG = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private DegreeProgramStateDao degreeProgramStateDao;

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

        try {
            if (!user.getRoleNames().isEmpty())
                jdbcTemplate.batchUpdate(sql("createUserRolesByRoleName"),
                        getUserRolesBatchArgs(user.getRoleNames(), user.getId()));
        } catch (Exception e) {
            throw new DaoException("Failed to create user roles", e);
        }
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
            setUserCurrentState(user);
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
            setUserCurrentState(user);
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
            setUserCurrentState(user);
            return user;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieve a {@link User} object by its panther id.
     *
     * @param pantherId
     * @return {@link User}
     */
    public User readByPantherId(String pantherId) {
        LOG.trace("Reading user with panther id {}", pantherId);
        try {
            User user = (User) this.jdbcTemplate.queryForObject(sql("readUserByPantherId"),
                    new MapSqlParameterSource("panther_id", pantherId), rowMapper);

            setUserRolesAndAuthorities(user);
            setUserCurrentState(user);
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
                    // TODO may not want to hardcode these column names here
                    roleNames.add(rs.getString("name"));
                    authorities.add(Authorities.valueOf(rs.getString("authority")));
                });
        user.setRoleNames(roleNames);
        user.setAuthorities(authorities);
    }

    private void setUserCurrentState(User user) {
        if (user.getCurrentStateId() == null) return;
        user.setCurrentState(degreeProgramStateDao.read(user.getCurrentStateId()));
    }

    private MapSqlParameterSource[] getUserRolesBatchArgs(Set<String> roleNames, Long userId) {
        MapSqlParameterSource[] batchArgs = new MapSqlParameterSource[roleNames.size()];

        int i = 0;
        for (String roleName : roleNames) {
            MapSqlParameterSource src = new MapSqlParameterSource();
            src.addValue("user_id", userId);
            src.addValue("role_name", roleName);
            batchArgs[i++] = src;
        }

        return batchArgs;
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

        Set<String> userRolesToDelete = read(user.getId()).getRoleNames();
        Set<String> userRolesToCreate = new HashSet<>();

        for (String roleName : user.getRoleNames()) {
            if (!userRolesToDelete.contains(roleName))
                userRolesToCreate.add(roleName);
            else
                userRolesToDelete.remove(roleName);
        }

        try {
            if (!userRolesToCreate.isEmpty())
                jdbcTemplate.batchUpdate(sql("createUserRolesByRoleName"),
                        getUserRolesBatchArgs(userRolesToCreate, user.getId()));

            if (!userRolesToDelete.isEmpty())
                jdbcTemplate.batchUpdate(sql("deleteUserRoleByUserIdAndRoleName"),
                        getUserRolesBatchArgs(userRolesToDelete, user.getId()));
        } catch (Exception e) {
            throw new DaoException("Failed to update user roles", e);
        }

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
