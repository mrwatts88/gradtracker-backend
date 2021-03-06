package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class UserRestController {

    static final String USER_PATH = "/user/";
    static final String USER_PANTHER_ID_PATH = USER_PATH + "panther_id/";
    static final String USER_EMAIL_PATH = USER_PATH + "email/";
    static final String USER_STATE_PATH = USER_PATH + "current_state/";

    private static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates the provided {@link User}.
     *
     * @param user     {@link User} to create
     * @param response {@link HttpServletResponse} that is sent back
     * @return created {@link User}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Create User")
    @PostMapping(value = USER_PATH)
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public User create(@RequestBody User user, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> userService.create(user), response, LOG);
    }

    /**
     * Get a {@link User} by its Id.
     *
     * @param userId   id of the {@link User} to read
     * @param response {@link HttpServletResponse} that is sent back
     * @return {@link User} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read User by ID")
    @GetMapping(value = USER_PATH + "{userId}")
    public User readById(@PathVariable Long userId, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> userService.read(userId), response, LOG);
    }

    /**
     * Get a {@link User} by its Panther Id.
     *
     * @param pantherId id of the {@link User} to read
     * @param response  {@link HttpServletResponse} that is sent back
     * @return {@link User} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read User by Panther ID")
    @GetMapping(value = USER_PANTHER_ID_PATH + "{pantherId}")
    public User readByPantherId(@PathVariable String pantherId, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> userService.readByPantherId(pantherId), response, LOG);
    }

    /**
     * Get a {@link User} by its Email.
     *
     * @param email    id of the {@link User} to read
     * @param response {@link HttpServletResponse} that is sent back
     * @return {@link User} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read User by Email")
    @GetMapping(value = USER_EMAIL_PATH + "{email}")
    public User readByEmail(@PathVariable String email, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> userService.readByEmail(email), response, LOG);
    }

    /**
     * Gets all the {@link User}s.
     *
     * @return list of {@link User}s retrieved from the database
     */
    @ApiOperation(value = "Read All Users")
    @GetMapping(value = USER_PATH)
    @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    public List<User> readAll() {
        return userService.readAll();
    }

    /**
     * Updates a {@link User} by its Id.
     *
     * @param userId   id of the {@link User} to update
     * @param user     updated {@link User}
     * @param response {@link HttpServletResponse} that is sent back
     * @return updated {@link User}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Update User by ID")
    @PutMapping(value = USER_PATH + "{userId}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public User update(@PathVariable Long userId, @RequestBody User user, @ApiIgnore HttpServletResponse response) throws IOException {
        user.setId(userId);
        return RestControllerUtil.runCallable(() -> userService.update(user), response, LOG);
    }

    /**
     * Updates a {@link User}'s current state by its Id.
     *
     * @param userId   id of the {@link User} to update
     * @param stateId  id of the {@link edu.uwm.capstone.model.DegreeProgramState} to set as the user's current state
     * @param response {@link HttpServletResponse} that is sent back
     * @return updated {@link User}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Update User by ID")
    @PutMapping(value = USER_STATE_PATH + "{userId}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public User updateCurrentState(@PathVariable Long userId, @RequestParam("stateId") Long stateId, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> userService.updateCurrentState(userId, stateId), response, LOG);
    }

    /**
     * Deletes a {@link User} by Id.
     *
     * @param userId   id of the {@link User} to update
     * @param response {@link HttpServletResponse} that is sent back
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Delete User by ID")
    @DeleteMapping(value = USER_PATH + "{userId}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public void deleteById(@PathVariable Long userId, @ApiIgnore HttpServletResponse response) throws IOException {
        RestControllerUtil.runRunnable(() -> userService.delete(userId), response, LOG);
    }
}
