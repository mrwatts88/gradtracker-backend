package edu.uwm.capstone.db;

import edu.uwm.capstone.model.Profile;
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
import java.util.Objects;

public class ProfileDao extends BaseDao<Long, Profile> {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileDao.class);

    /**
     * Create a {@link Profile} object.
     *
     * @param profile {@link Profile}
     * @return {@link Profile}
     */
    @Override
    public Profile create(Profile profile) {
        if (profile == null) {
            throw new DaoException("Request to create a new Profile received null");
        } else if (profile.getId() != null) {
            throw new DaoException("When creating a new Profile the id should be null, but was set to " + profile.getId());
        }

        LOG.trace("Creating profile {}", profile);

        profile.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createProfile"),
                new MapSqlParameterSource(rowMapper.mapObject(profile)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create profile %s - affected %s rows", profile.toString(), result));
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        profile.setId(id);
        return profile;
    }

    /**
     * Retrieve a {@link Profile} object by its Id.
     *
     * @param profileId
     * @return {@link Profile}
     */
    @Override
    public Profile read(Long profileId) {
        LOG.trace("Reading profile {}", profileId);
        try {
            return (Profile) this.jdbcTemplate.queryForObject(sql("readProfile"), new MapSqlParameterSource("id", profileId), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Retrieve a {@link Profile} object by its email.
     *
     * @param email
     * @return {@link Profile}
     */
    public Profile readByEmail(String email){
        LOG.trace("Reading profile with email {}", email);
        try {
            return (Profile) this.jdbcTemplate.queryForObject(sql("readProfileByEmail"), new MapSqlParameterSource("email", email), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Update the provided {@link Profile} object.
     *
     * @param profile {@link Profile}
     * @return true if successful
     */
    @Override
    public boolean update(Profile profile) {
        LOG.trace("Updating profile {}", profile);
        profile.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateProfile"), new MapSqlParameterSource(rowMapper.mapObject(profile)));

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update profile %s - affected %s rows", profile.toString(), result));
        }
        return true;
    }

    /**
     * Delete a {@link Profile} object by its Id.
     *
     * @param profileId
     * @return true if successful
     */
    @Override
    public boolean delete(Long profileId) {
        LOG.trace("Deleting profile {}", profileId);
        int result = this.jdbcTemplate.update(sql("deleteProfile"), new MapSqlParameterSource("id", profileId));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete profile %s affected %s rows", profileId, result));
        }
        return true;
    }
}
