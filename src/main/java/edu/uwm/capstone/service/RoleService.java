package edu.uwm.capstone.service;

import edu.uwm.capstone.db.RoleDao;
import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("roleService")
public class RoleService {

    private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

    private final RoleDao roleDao;

    @Autowired
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    /**
     * Create a {@link Role} object.
     *
     * @param role {@link Role}
     * @return {@link Role}
     */
    public Role create(Role role) {
        LOG.trace("Creating role {}", role);

        checkValidRole(role, true);

        return roleDao.create(role);
    }

    /**
     * Retrieve a {@link Role} object by its Id.
     *
     * @param roleId
     * @return {@link Role}
     */
    public Role read(Long roleId) {
        LOG.trace("Reading role {}", roleId);

        Role role = roleDao.read(roleId);

        if (role == null) {
            throw new EntityNotFoundException("Role with ID: " + roleId + " not found.");
        }
        return role;
    }

    /**
     * Retrieve a {@link Role} object by its email.
     *
     * @param name
     * @return {@link Role}
     */
    public Role readByName(String name) {
        LOG.trace("Reading role by name {}", name);

        Role role = roleDao.readByName(name);

        if (role == null) {
            throw new EntityNotFoundException("Role with name: " + name + " not found.");
        }
        return role;
    }

    /**
     * Retrieves all the {@link Role} objects.
     *
     * @return List of {@link Role}
     */
    public List<Role> readAll() {
        LOG.trace("Reading all roles");

        return roleDao.readAll();
    }

    /**
     * Update the provided {@link Role} object.
     *
     * @param role {@link Role}
     * @return true if successful
     */
    public Role update(Role role) {
        LOG.trace("Updating role {}", role);

        checkValidRole(role, false);

        Role roleInDb = roleDao.read(role.getId());
        if (roleInDb == null) {
            throw new EntityNotFoundException("Could not update Role " + role.getId() + " - record not found.");
        }
        role.setCreatedDate(roleInDb.getCreatedDate());
        return roleDao.update(role);
    }

    /**
     * Delete a {@link Role} object by its Id.
     *
     * @param roleId
     * @return true if successful
     */
    public void delete(Long roleId) {
        LOG.trace("Deleting role {}", roleId);

        if (roleDao.read(roleId) == null) {
            throw new EntityNotFoundException("Could not delete Role " + roleId + " - record not found.");
        }
        roleDao.delete(roleId);
    }

    /**
     * Check if a {@link Role} object is valid.
     * If checkNullId is true, asserts that the {@link Role} ID must be null.
     *
     * @param role
     * @param checkNullId
     */
    private void checkValidRole(Role role, boolean checkNullId) {
        Assert.notNull(role, "Role must not be null");
        if (checkNullId)
            Assert.isNull(role.getId(), "Role ID must be null");

        Assert.isNull(roleDao.readByName(role.getName()), "Role already exists with the name " + role.getName());
    }
}
