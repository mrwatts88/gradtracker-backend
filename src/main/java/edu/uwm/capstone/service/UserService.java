package edu.uwm.capstone.service;

import edu.uwm.capstone.db.UserDao;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service("userService")
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;

    private final UserDao userDao;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
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

        // TODO check if email is legit
        //  check if email already exists in db,
        //  check if panther id already exits in db

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.create(user);
    }

    /**
     * Retrieve a {@link User} object by its Id.
     *
     * @param userId
     * @return {@link User}
     */
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
        Assert.notNull(user.getFirstName(), "User last name must not be null");
        Assert.notNull(user.getFirstName(), "User panther id must not be null");
        Assert.isNull(userDao.readByEmail(user.getEmail()), "User email must not exist in the DB when creating");
    }
}
