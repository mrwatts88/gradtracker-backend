package edu.uwm.capstone.controller;

import edu.uwm.capstone.db.FormDefinitionDao;
import edu.uwm.capstone.model.FormDefinition;
import edu.uwm.capstone.service.FormDefinitionService;
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
public class FormDefinitionRestController {

    static final String FORM_DEF_PATH = "/formDef/";

    private static final Logger LOG = LoggerFactory.getLogger(FormDefinitionRestController.class);

    private final FormDefinitionService formDefinitionService;

    @Autowired
    public FormDefinitionRestController(FormDefinitionService formDefinitionService) {
        this.formDefinitionService = formDefinitionService;
    }

    /**
     * Creates the provided {@link FormDefinition}.
     *
     * @param formDef  {@link FormDefinition} to create
     * @param response {@link HttpServletResponse} that is sent back
     * @return created {@link FormDefinition}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Create Form Definition")
    @PostMapping(value = FORM_DEF_PATH)
    @PreAuthorize("hasAuthority('CREATE_FORM_DEF')")
    public FormDefinition create(@RequestBody FormDefinition formDef, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> formDefinitionService.create(formDef), response, LOG);
    }

    /**
     * Get a {@link FormDefinition} by its Id.
     *
     * @param formDefId id of the {@link FormDefinition} to read
     * @param response  {@link HttpServletResponse} that is sent back
     * @return {@link FormDefinition} retrieved from the database
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Read Form Definition by ID")
    @GetMapping(value = FORM_DEF_PATH + "{formDefId}")
    public FormDefinition readById(@PathVariable Long formDefId, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> formDefinitionService.read(formDefId), response, LOG);
    }

    /**
     * Gets all {@link FormDefinition}s.
     *
     * @return list of {@link FormDefinition}s retrieved from the database
     */
    @ApiOperation(value = "Read All Form Definitions")
    @GetMapping(value = FORM_DEF_PATH)
//    @PreAuthorize("hasAuthority('TEST')") // TODO figure out how we can use the Authorities enum here
    @PreAuthorize("hasAuthority('READ_ALL_FORMS_DEF')")
    public List<FormDefinition> readAll() {
        return formDefinitionService.readAll();
    }

    /**
     * Updates a {@link FormDefinition} by its Id.
     *
     * @param formDefId      id of the {@link FormDefinition} to update
     * @param formDefinition updated {@link FormDefinition}
     * @param response       {@link HttpServletResponse} that is sent back
     * @return updated {@link FormDefinition}
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Update Form Definition by ID")
    @PutMapping(value = FORM_DEF_PATH + "{formDefId}")
    @PreAuthorize("hasAuthority('UPDATE_FORM_DEF')")
    public FormDefinition update(@PathVariable Long formDefId, @RequestBody FormDefinition formDefinition, @ApiIgnore HttpServletResponse response) throws IOException {
        formDefinition.setId(formDefId);
        
        return RestControllerUtil.runCallable(() -> formDefinitionService.update(formDefinition), response, LOG);
    }

    /**
     * Deletes a {@link FormDefinition} by Id.
     *
     * @param formDefId id of the {@link FormDefinition} to update
     * @param response  {@link HttpServletResponse} that is sent back
     * @throws IOException if error response cannot be created
     */
    @ApiOperation(value = "Delete FormDefinition by ID")
    @DeleteMapping(value = FORM_DEF_PATH + "{formDefId}")
    @PreAuthorize("hasAuthority('DELETE_FORM_DEF')")
    public void deleteById(@PathVariable Long formDefId, @ApiIgnore HttpServletResponse response) throws IOException {
        RestControllerUtil.runRunnable(() -> formDefinitionService.delete(formDefId), response, LOG);
    }
}
