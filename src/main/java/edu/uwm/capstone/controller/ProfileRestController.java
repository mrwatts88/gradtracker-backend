package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.Profile;
import edu.uwm.capstone.service.ProfileService;
import edu.uwm.capstone.sql.exception.ServiceException;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class ProfileRestController {

    public static final String PROFILE_PATH = "/profile/";

    private static final Logger LOG = LoggerFactory.getLogger(ProfileRestController.class);

    private final ProfileService profileService;

    @Autowired
    public ProfileRestController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Creates the provided {@link Profile}
     *
     * @param profile  {@link Profile}
     * @param response {@link HttpServletResponse}
     * @return {@link Profile}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Create Profile")
    @PostMapping(value = PROFILE_PATH)
    public Profile create(@RequestBody Profile profile, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            Assert.isNull(profile.getId(), "Profile ID must be null");
            return profileService.create(profile);
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
     * Get the {@link Profile} by Id
     *
     * @param profileId {@link Profile#getId()}
     * @param response  {@link HttpServletResponse}
     * @return {@link Profile} retrieved from the database
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Read Profile by ID")
    @GetMapping(value = PROFILE_PATH + "{profileId}")
    public Profile readById(@PathVariable Long profileId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return profileService.read(profileId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        }
    }

    /**
     * Updates the provided {@link Profile}
     *
     * @param profile  {@link Profile}
     * @param response {@link HttpServletResponse}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Update Profile")
    @PutMapping(value = PROFILE_PATH)
    public void update(@RequestBody Profile profile, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            Assert.notNull(profile.getId(), "Profile Id must not be null");
            profileService.update(profile);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
        } catch (ServiceException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Delete the {@link Profile} by Id
     *
     * @param profileId {@link Profile#getId()}
     * @param response  {@link HttpServletResponse}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Delete Profile by ID")
    @DeleteMapping(value = PROFILE_PATH + "{profileId}")
    public void deleteById(@PathVariable Long profileId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            Assert.notNull(profileId, "Profile Id must not be null");
            profileService.delete(profileId);
        } catch (IllegalArgumentException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
        } catch (ServiceException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
