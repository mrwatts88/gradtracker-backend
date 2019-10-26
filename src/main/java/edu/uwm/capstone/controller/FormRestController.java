package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.service.FormService;
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

public class FormRestController {
    static final String FORM_PATH = "/form/";

    private static final Logger LOG = LoggerFactory.getLogger(FormRestController.class);

    private final FormService formService;

    @Autowired
    public FormRestController(FormService formService) {
        this.formService = formService;
    }

    /**
     * Creates the provided {@link Form}
     *
     * @param form  {@link Form}
     * @param response {@link HttpServletResponse}
     * @return {@link Form}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Create Form")
    @PostMapping(value = FORM_PATH)
    public Form create(@RequestBody Form form, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return formService.create(form);
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
     * Get the {@link Form} by Id
     *
     * @param formId {@link Form#getId()}
     * @param response  {@link HttpServletResponse}
     * @return {@link Form} retrieved from the database
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Read Form by ID")
    @GetMapping(value = FORM_PATH + "{formId}")
    public Form readById(@PathVariable Long formId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return formService.read(formId);
        } catch (EntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        }
    }

    /**
     * Gets all the {@link Form}
     *
     * @param response {@link HttpServletResponse}
     * @return {@link Form} retrieved from the database
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Read All Form")
    @GetMapping(value = FORM_PATH)
    public List<Form> readAll(@ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return formService.readAll();
        } catch (EntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        }
    }

    /**
     * Gets all the {@link Form} that were submitted by a User with Id = userId.
     *
     * @param userId
     * @param response {@link HttpServletResponse}
     * @return {@link Form} retrieved from the database
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Read All Form")
    @GetMapping(value = FORM_PATH + "/user/{userId}")
    public List<Form> readAllByUserId(@PathVariable Long userId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            return formService.readAllByUserId(userId);
        } catch (EntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            return null;
        }
    }

    /**
     * Updates the provided {@link Form} by Id
     *
     * @param formId      {@link Form#getId()}
     * @param form {@link Form}
     * @param response       {@link HttpServletResponse}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Update Form by ID")
    @PutMapping(value = FORM_PATH + "{formId}")
    public void update(@PathVariable Long formId, @RequestBody Form form, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            form.setId(formId);
            formService.update(form);
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

    /**
     * Delete the {@link Form} by Id
     *
     * @param formId {@link Form#getId()}
     * @param response  {@link HttpServletResponse}
     * @throws IOException if error response cannot be created.
     */
    @ApiOperation(value = "Delete Form by ID")
    @DeleteMapping(value = FORM_PATH + "{formId}")
    public void deleteById(@PathVariable Long formId, @ApiIgnore HttpServletResponse response) throws IOException {
        try {
            formService.delete(formId);
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
