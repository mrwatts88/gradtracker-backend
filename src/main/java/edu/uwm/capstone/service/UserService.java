package edu.uwm.capstone.service;

import edu.uwm.capstone.db.DegreeProgramDao;
import edu.uwm.capstone.db.RoleDao;
import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.model.Role;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("userService")
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final DegreeProgramDao degreeProgramDao;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserDao userDao, RoleDao roleDao, DegreeProgramDao degreeProgramDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.degreeProgramDao = degreeProgramDao;
    }

    /**
     * Create a {@link User} object.
     *
     * @param user {@link User}
     * @return {@link User}
     */
    public User create(User user) {
        LOG.trace("Creating user {}", user);

        checkValidUser(user, true);

        if (user.getDegreeProgramName() != null) {
            DegreeProgram dp = degreeProgramDao.readByName(user.getDegreeProgramName());
            Assert.notNull(dp, "Degree Program: " + user.getDegreeProgramName() + " not found.");
            user.setCurrentState(dp.initialState());
        } else {
            user.setCurrentState(null);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.create(user);
    }

    /**
     * Retrieve a {@link User} object by its Id.
     *
     * @param userId
     * @return {@link User}
     */
    //TODO: Check that users who can't read all only read their own IDs
    public User read(Long userId) {
        LOG.trace("Reading user {}", userId);

        User user = userDao.read(userId);

        if (user == null) {
            throw new EntityNotFoundException("User with ID: " + userId + " not found.");
        }
        return user;
    }

    /**
     * Retrieve a {@link User} object by its email.
     *
     * @param email
     * @return {@link User}
     */
    public User readByEmail(String email) {
        LOG.trace("Reading user by email {}", email);

        User user = userDao.readByEmail(email);

        if (user == null) {
            throw new EntityNotFoundException("User with email: " + email + " not found.");
        }
        return user;
    }

    /**
     * Retrieve a {@link User} object by its email.
     *
     * @param pantherId
     * @return {@link User}
     */
    public User readByPantherId(String pantherId) {
        LOG.trace("Reading user by panther id {}", pantherId);

        User user = userDao.readByPantherId(pantherId);

        if (user == null) {
            throw new EntityNotFoundException("User with panther id: " + pantherId + " not found.");
        }
        return user;
    }

    /**
     * Retrieves all the {@link User} objects.
     *
     * @return List of {@link User}
     */
    public List<User> readAll() {
        LOG.trace("Reading all users");

        return userDao.readAll();
    }

    /**
     * Update the provided {@link User} object.
     *
     * @param user {@link User}
     * @return true if successful
     */
    public User update(User user) {
        LOG.trace("Updating user {}", user);

        checkValidUser(user, false);

        User userInDb = userDao.read(user.getId());
        if (userInDb == null) {
            throw new EntityNotFoundException("Could not update User " + user.getId() + " - record not found.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(userInDb.getCreatedDate());
        return userDao.update(user);
    }

    /**
     * Delete a {@link User} object by its Id.
     *
     * @param userId
     * @return true if successful
     */
    public void delete(Long userId) {
        LOG.trace("Deleting user {}", userId);

        if (userDao.read(userId) == null) {
            throw new EntityNotFoundException("Could not delete User " + userId + " - record not found.");
        }
        userDao.delete(userId);
    }

    /**
     * Check if a {@link User} object is valid.
     * If checkNullId is true, asserts that the {@link User} ID must be null.
     *
     * @param user
     * @param checkNullId
     */
    private void checkValidUser(User user, boolean checkNullId) {
        Assert.notNull(user, "User must not be null");
        if (checkNullId)
            Assert.isNull(user.getId(), "User ID must be null");
        Assert.notNull(user.getEmail(), "User email must not be null");
        Assert.notNull(user.getPassword(), "User password must not be null");
        Assert.notNull(user.getFirstName(), "User first name must not be null");
        Assert.notNull(user.getLastName(), "User last name must not be null");
        Assert.notNull(user.getPantherId(), "User panther id must not be null");
        Assert.notNull(user.getRoleNames(), "User roles must not be null");

        User tmpUser = userDao.readByEmail(user.getEmail());
        Assert.isTrue(tmpUser == null || tmpUser.getId().equals(user.getId()),
                "User already registered with email " + user.getEmail());

        tmpUser = userDao.readByPantherId(user.getPantherId());
        Assert.isTrue(tmpUser == null || tmpUser.getId().equals(user.getId()),
                "User already registered with panther id " + user.getPantherId());

        Set<String> roleNamesInDb = roleDao.readAll().stream().map(Role::getName).collect(Collectors.toCollection(HashSet::new));
        for (String roleName : user.getRoleNames()) {
            Assert.isTrue(roleNamesInDb.contains(roleName), "User roles must exist in the database. Found role " + roleName + " which doesn't exist");
        }

    }
}
