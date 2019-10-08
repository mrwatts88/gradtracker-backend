package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.UserService;
import edu.uwm.capstone.service.exception.UserNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class UserRestController {

    static final String PROFILE_PATH = "/profile/";

    private static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates the provided {@link User}
     *
     * @param user  {@link User}
     * @param response {@link HttpServletResponse}
     * @return {@link User}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Create Profile")
    @PostMapping(value = PROFILE_PATH)
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
     * Get the {@link User} by Id
     *
     * @param profileId {@link User#getId()}
     * @param response  {@link HttpServletResponse}
     * @return {@link User} retrieved from the database
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Read Profile by ID")
    @GetMapping(value = PROFILE_PATH + "{profileId}")
    public User readById(@PathVariable Long profileId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return userService.read(profileId);
        } catch (UserNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        }
    }

    /**
     * Updates the provided {@link User}
     *
     * @param user  {@link User}
     * @param response {@link HttpServletResponse}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Update Profile")
    @PutMapping(value = PROFILE_PATH)
    public void update(@RequestBody User user, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            userService.update(user);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
        } catch (UserNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Delete the {@link User} by Id
     *
     * @param profileId {@link User#getId()}
     * @param response  {@link HttpServletResponse}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Delete Profile by ID")
    @DeleteMapping(value = PROFILE_PATH + "{profileId}")
    public void deleteById(@PathVariable Long profileId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            userService.delete(profileId);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
        } catch (UserNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
