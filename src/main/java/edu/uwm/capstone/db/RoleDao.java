package edu.uwm.capstone.db;

import com.google.common.collect.Sets;
import edu.uwm.capstone.model.Role;
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
import java.util.Set;

public class RoleDao extends BaseDao<Long, Role> {

    private static final Logger LOG = LoggerFactory.getLogger(RoleDao.class);

    /**
     * Create a {@link Role} object.
     *
     * @param role {@link Role}
     * @return {@link Role}
     */
    @Override
    public Role create(Role role) {
        if (role == null) {
            throw new DaoException("Request to create a new role received null");
        } else if (role.getId() != null) {
            throw new DaoException("When creating a new role the id should be null, but was set to " + role.getId());
        }

        LOG.trace("Creating role {}", role);

        role.setCreatedDate(LocalDateTime.now());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = this.jdbcTemplate.update(sql("createRole"),
                new MapSqlParameterSource(rowMapper.mapObject(role)), keyHolder, new String[]{BaseRowMapper.BaseColumnType.ID.name()});

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to create role %s - affected %s rows", role.toString(), result));
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        role.setId(id);

        try {
            if (!role.getAuthorities().isEmpty())
                jdbcTemplate.batchUpdate(sql("createRoleAuthority"),
                        getRoleAuthoritiesBatchArgs(role.getAuthorities(), role.getId()));
        } catch (Exception e) {
            throw new DaoException("failed to create role authorities", e);
        }

        return role;
    }

    /**
     * Retrieve a {@link Role} object by its Id.
     *
     * @param roleId
     * @return {@link Role}
     */
    @Override
    public Role read(Long roleId) {
        LOG.trace("Reading role {}", roleId);
        try {
            Role role = (Role) this.jdbcTemplate.queryForObject(sql("readRoleById"), new MapSqlParameterSource("id", roleId), rowMapper);

            role.setAuthorities(Sets.newHashSet(jdbcTemplate.queryForList(sql("readRoleAuthoritiesByRoleId"),
                    new MapSqlParameterSource("role_id", role.getId()),
                    Authorities.class))
            );
            return role;

        } catch (NullPointerException | EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Returns a list of all {@link Role} objects from the database.
     *
     * @return
     */
    public List<Role> readAll() {
        LOG.trace("Reading all roles");
        List<Role> roles = this.jdbcTemplate.query(sql("readAllRoles"), rowMapper);

        for (Role role : roles) {
            role.setAuthorities(Sets.newHashSet(jdbcTemplate.queryForList(sql("readRoleAuthoritiesByRoleId"),
                    new MapSqlParameterSource("role_id", role.getId()),
                    Authorities.class)));
        }

        return roles;
    }

    /**
     * Retrieve a {@link Role} object by its email.
     *
     * @param name
     * @return {@link Role}
     */
    public Role readByName(String name) {
        LOG.trace("Reading role with name {}", name);
        try {
            Role role = (Role) this.jdbcTemplate.queryForObject(sql("readRoleByName"), new MapSqlParameterSource("role_name", name), rowMapper);

            role.setAuthorities(Sets.newHashSet(jdbcTemplate.queryForList(sql("readRoleAuthoritiesByRoleId"),
                    new MapSqlParameterSource("role_id", role.getId()),
                    Authorities.class))
            );
            return role;

        } catch (NullPointerException | EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Update the provided {@link Role} object.
     *
     * @param role {@link Role}
     * @return true if successful
     */
    @Override
    public Role update(Role role) {
        if (role == null) {
            throw new DaoException("Request to update a new role received null");
        } else if (role.getId() == null) {
            throw new DaoException("When updating a new role the id should not be null");
        }

        LOG.trace("Updating role {}", role);
        role.setUpdatedDate(LocalDateTime.now());
        int result = this.jdbcTemplate.update(sql("updateRole"), new MapSqlParameterSource(rowMapper.mapObject(role)));

        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to update role %s - affected %s rows", role.toString(), result));
        }

        Set<Authorities> authoritiesToDelete = read(role.getId()).getAuthorities();
        Set<Authorities> authoritiesToCreate = new HashSet<>();

        for (Authorities authority : role.getAuthorities()) {
            if (!authoritiesToDelete.contains(authority))
                authoritiesToCreate.add(authority);
            else
                authoritiesToDelete.remove(authority);
        }

        try {
            if (!authoritiesToCreate.isEmpty())
                jdbcTemplate.batchUpdate(sql("createRoleAuthority"),
                        getRoleAuthoritiesBatchArgs(authoritiesToCreate, role.getId()));

            if (!authoritiesToDelete.isEmpty())
                jdbcTemplate.batchUpdate(sql("deleteRoleAuthorityByNameAndRoleId"),
                        getRoleAuthoritiesBatchArgs(authoritiesToDelete, role.getId()));
        } catch (Exception e) {
            throw new DaoException("failed to update role authorities", e);
        }

        return role;
    }

    private MapSqlParameterSource[] getRoleAuthoritiesBatchArgs(Set<Authorities> authorities, Long roleId) {
        MapSqlParameterSource[] batchArgs = new MapSqlParameterSource[authorities.size()];

        int i = 0;
        for (Authorities roleAuthority : authorities) {
            MapSqlParameterSource src = new MapSqlParameterSource();
            src.addValue("authority", roleAuthority.toString());
            src.addValue("role_id", roleId);
            batchArgs[i++] = src;
        }

        return batchArgs;
    }

    /**
     * Delete a {@link Role} object by its Id.
     *
     * @param roleId
     * @return true if successful
     */
    @Override
    public void delete(Long roleId) {
        LOG.trace("Deleting role {}", roleId);
        this.jdbcTemplate.update(sql("deleteUserRolesByRoleId"), new MapSqlParameterSource("role_id", roleId));
        this.jdbcTemplate.update(sql("deleteRoleAuthoritiesByRoleId"), new MapSqlParameterSource("role_id", roleId));
        int result = this.jdbcTemplate.update(sql("deleteRole"), new MapSqlParameterSource("id", roleId));
        if (result != 1) {
            throw new DaoException(String.format("Failed attempt to delete role %s affected %s rows", roleId, result));
        }
    }
}
