package edu.uwm.capstone.controller;

import edu.uwm.capstone.model.DegreeProgram;
import edu.uwm.capstone.service.DegreeProgramService;
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
public class DegreeProgramRestController {

    static final String DEGREE_PROGRAM_PATH = "/degreeProgram/";

    private static final Logger LOG = LoggerFactory.getLogger(DegreeProgramRestController.class);

    private final DegreeProgramService degreeProgramService;

    @Autowired
    public DegreeProgramRestController(DegreeProgramService dpService) {
        this.degreeProgramService = dpService;
    }

    @ApiOperation(value = "Create Degree Program")
    @PostMapping(value = DEGREE_PROGRAM_PATH)
    public DegreeProgram create(@RequestBody DegreeProgram dp, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> degreeProgramService.create(dp), response, LOG);
    }

    @ApiOperation(value = "Read Degree Program by ID")
    @GetMapping(value = DEGREE_PROGRAM_PATH + "{degreeProgramId}")
    public DegreeProgram readById(@PathVariable Long degreeProgramId, @ApiIgnore HttpServletResponse response) throws IOException {
        return RestControllerUtil.runCallable(() -> degreeProgramService.read(degreeProgramId), response, LOG);
    }

    

    /*
    @ApiOperation(value = "Read All Form Definitions")
    @GetMapping(value = FORM_DEF_PATH)
//    @PreAuthorize("hasAuthority('TEST')") // TODO figure out how we can use the Authorities enum here
    public List<FormDefinition> readAll() {
        return formDefinitionService.readAll();
    }


    @ApiOperation(value = "Update Form Definition by ID")
    @PutMapping(value = FORM_DEF_PATH + "{formDefId}")
    public FormDefinition update(@PathVariable Long formDefId, @RequestBody FormDefinition formDefinition, @ApiIgnore HttpServletResponse response) throws IOException {
        formDefinition.setId(formDefId);
        return RestControllerUtil.runCallable(() -> formDefinitionService.update(formDefinition), response, LOG);
    }


    @ApiOperation(value = "Delete FormDefinition by ID")
    @DeleteMapping(value = FORM_DEF_PATH + "{formDefId}")
    public void deleteById(@PathVariable Long formDefId, @ApiIgnore HttpServletResponse response) throws IOException {
        RestControllerUtil.runRunnable(() -> formDefinitionService.delete(formDefId), response, LOG);
    }
 */



}
