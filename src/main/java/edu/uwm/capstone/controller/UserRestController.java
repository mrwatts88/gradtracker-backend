package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.UserService;
import edu.uwm.capstone.service.exception.EntityNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class UserRestController {

    static final String USER_PATH = "/user/";

    private static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates the provided {@link User}.
     *
     * @param user  {@link User} to create
     * @param response {@link HttpServletResponse} that is sent back
     * @return created {@link User}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Create User")
    @PostMapping(value = USER_PATH)
    public User create(@RequestBody User user, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return userService.create(user);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            return null;
        }
    }

    /**
     * Get a {@link User} by its Id.
     *
     * @param userId id of the {@link User} to read
     * @param response  {@link HttpServletResponse} that is sent back
     * @return {@link User} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read User by ID")
    @GetMapping(value = USER_PATH + "{userId}")
    public User readById(@PathVariable Long userId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return userService.read(userId);
        } catch (EntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        }
    }

    /**
     * Gets all the {@link User}s
     *
     * @param response {@link HttpServletResponse}
     * @return list of {@link User}s retrieved from the database
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Read All Users")
    @GetMapping(value = USER_PATH)
    public List<User> readAll(@ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return userService.readAll();
        } catch (EntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        }
    }

    /**
     * Updates a {@link User} by its Id.
     *
     * @param userId      id of the {@link User} to update
     * @param user updated {@link User}
     * @param response       {@link HttpServletResponse} that is sent back
     * @return updated {@link User}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Update User by ID")
    @PutMapping(value = USER_PATH + "{userId}")
    public User update(@PathVariable Long userId, @RequestBody User user, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            user.setId(userId);
            return userService.update(user);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
        } catch (EntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return null;
    }

    /**
     * Deletes a {@link User} by Id.
     *
     * @param userId id of the {@link User} to update
     * @param response  {@link HttpServletResponse} that is sent back
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Delete User by ID")
    @DeleteMapping(value = USER_PATH + "{userId}")
    public void deleteById(@PathVariable Long userId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            userService.delete(userId);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
        } catch (EntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
