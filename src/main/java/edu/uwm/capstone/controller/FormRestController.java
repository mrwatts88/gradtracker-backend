package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.Form;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.model.User;
import edu.uwm.capstone.service.FormService;
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
     * Creates the provided {@link Form}.
     *
     * @param form     {@link Form} to create
     * @param response {@link HttpServletResponse} that is sent back
     * @return created {@link Form}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Create Form")
    @PostMapping(value = FORM_PATH)
    public Form create(@RequestBody Form form, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> formService.create(form), response, LOG);
    }

    /**
     * Get a {@link Form} by its Id.
     *
     * @param formId   id of the {@link Form} to read
     * @param response {@link HttpServletResponse} that is sent back
     * @return {@link Form} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read Form by ID")
    @GetMapping(value = FORM_PATH + "{formId}")
    public Form readById(@PathVariable Long formId, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> formService.read(formId), response, LOG);
    }

    /**
     * Gets all {@link Form}s.
     *
     * @return list of {@link Form}s retrieved from the database
     */
    @ApiOperation(value = "Read All Forms")
    @GetMapping(value = FORM_PATH)
    public List<Form> readAll() {
        return formService.readAll();
    }

    /**
     * Gets all {@link Form}s belonging to the {@link User} with the given Panther ID.
     * @param pantherId
     * @return
     */
    @ApiOperation(value = "Read All Forms by User's PantherID")
    @GetMapping(value = FORM_PATH + "panther_id/{pantherId}")
    public List<Form> readAllByPantherId(@PathVariable String pantherId) {
        return formService.readAllByPantherId(pantherId);
    }


    /**
     * Gets all {@link Form}s that have a {@link Form#getFormDefId()} = formDefId.
     *
     * @param formDefId id of the {@link FormDefinition}
     * @return list of {@link Form}s retrieved from the database
     */
    @ApiOperation(value = "Read All Forms by Form Definition Id")
    @GetMapping(value = FORM_PATH + "/formDef/{formDefId}")
    public List<Form> readAllByFormDefId(@PathVariable Long formDefId) {
        return formService.readAllByFormDefId(formDefId);
    }

    /**
     * Gets all {@link Form}s that have a {@link Form#getUserId()} = userId.
     *
     * @param userId   id of the {@link User}
     * @return list of {@link Form}s retrieved from the database
     */
    @ApiOperation(value = "Read All Forms by User Id")
    @GetMapping(value = FORM_PATH + "/user/{userId}")
    public List<Form> readAllByUserId(@PathVariable Long userId) {
        return formService.readAllByUserId(userId);
    }

    /**
     * Updates a {@link Form} by its Id.
     *
     * @param formId   id of the {@link Form} to update
     * @param form     updated {@link Form}
     * @param response {@link HttpServletResponse} that is sent back
     * @return updated {@link Form}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Update Form by ID")
    @PutMapping(value = FORM_PATH + "{formId}")
    public Form update(@PathVariable Long formId, @RequestBody Form form, @ApiIgnore HttpServletResponse response) throws IOException {
        form.setId(formId);
        return RestControllerUtil.runCallable(() -> formService.update(form), response, LOG);
    }

    /**
     * Deletes a {@link FormDefinition} by Id.
     *
     * @param formId   id of the {@link Form} to update
     * @param response {@link HttpServletResponse} that is sent back
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Delete Form by ID")
    @DeleteMapping(value = FORM_PATH + "{formId}")
    public void deleteById(@PathVariable Long formId, @ApiIgnore HttpServletResponse response) throws IOException {
        RestControllerUtil.runRunnable(() -> formService.delete(formId), response, LOG);
    }

    /**
     * Approve or Reject a form submission
     *
     * @param formId   id of the {@link Form} to update
     * @param approve form is approved, true or false
     * @param response {@link HttpServletResponse} that is sent back
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Approve/Reject form by ID")
    @PutMapping(value = FORM_PATH + "{formId}")
    public void approve(@PathVariable Long formId, @RequestParam(name = approve) boolean approved, @ApiIgnore HttpServletResponse response) throws IOException {
        RestControllerUtil.runRunnable(() -> formService.delete(formId), response, LOG);
    }

}
