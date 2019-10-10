package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.FormDefinitionService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class FormDefinitionRestController {

    static final String FORM_DEF_PATH = "/formDef/";

    private static final Logger LOG = LoggerFactory.getLogger(FormDefinitionRestController.class);

    private final FormDefinitionService formDefinitionService;

    @Autowired
    public FormDefinitionRestController(FormDefinitionService formDefinitionService) {
        this.formDefinitionService = formDefinitionService;
    }

    /**
     * Creates the provided {@link User}
     *
     * @param formDef  {@link User}
     * @param response {@link HttpServletResponse}
     * @return {@link User}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Create Form")
    @PostMapping(value = FORM_DEF_PATH)
    public FormDefinition create(@RequestBody FormDefinition formDef, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return formDefinitionService.create(formDef);
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

//    /**
//     * Get the {@link User} by Id
//     *
//     * @param userId {@link User#getId()}
//     * @param response  {@link HttpServletResponse}
//     * @return {@link User} retrieved from the database
//     * @throws IOException if error response cannot be created.
//     */
//    @ApiOperation(value = "Read User by ID")
//    @GetMapping(value = FORM_DEF_PATH + "{userId}")
//    public User readById(@PathVariable Long userId, @ApiIgnore HttpServletResponse response) throws IOException {
//        try {
//            return formDefinitionService.read(userId);
//        } catch (UserNotFoundException e) {
//            LOG.error(e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//            return null;
//        }
//    }
//
//    /**
//     * Updates the provided {@link User}
//     *
//     * @param user  {@link User}
//     * @param response {@link HttpServletResponse}
//     * @throws IOException if error response cannot be created.
//     */
//    @ApiOperation(value = "Update User")
//    @PutMapping(value = FORM_DEF_PATH)
//    public void update(@RequestBody User user, @ApiIgnore HttpServletResponse response) throws IOException {
//        try {
//            formDefinitionService.update(user);
//        } catch (IllegalArgumentException e) {
//            LOG.error(e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
//        } catch (UserNotFoundException e) {
//            LOG.error(e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
//
//    /**
//     * Delete the {@link User} by Id
//     *
//     * @param userId {@link User#getId()}
//     * @param response  {@link HttpServletResponse}
//     * @throws IOException if error response cannot be created.
//     */
//    @ApiOperation(value = "Delete User by ID")
//    @DeleteMapping(value = FORM_DEF_PATH + "{userId}")
//    public void deleteById(@PathVariable Long userId, @ApiIgnore HttpServletResponse response) throws IOException {
//        try {
//            formDefinitionService.delete(userId);
//        } catch (IllegalArgumentException e) {
//            LOG.error(e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, e.getMessage());
//        } catch (UserNotFoundException e) {
//            LOG.error(e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
//        } catch (Exception e) {
//            LOG.error(e.getMessage(), e);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }
}
