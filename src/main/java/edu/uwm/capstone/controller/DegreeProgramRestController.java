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
import java.util.List;

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

    @ApiOperation(value = "Read All Degree Programs.")
    @GetMapping(value = DEGREE_PROGRAM_PATH)
    public List<DegreeProgram> readAll() {
        return degreeProgramService.readAll();
    }

    @ApiOperation(value = "Update Degree Program by ID")
    @PutMapping(value = DEGREE_PROGRAM_PATH + "{degreeProgramId}")
    public DegreeProgram update(@PathVariable Long degreeProgramId, @RequestBody DegreeProgram dp,
                                @ApiIgnore HttpServletResponse response) throws IOException {
        dp.setId(degreeProgramId);
        return RestControllerUtil.runCallable(() -> degreeProgramService.update(dp), response, LOG);
    }

    @ApiOperation(value = "Delete Degree Program by ID")
    @DeleteMapping(value = DEGREE_PROGRAM_PATH + "{degreeProgramId}")
    public void deleteById(@PathVariable Long degreeProgramId, @ApiIgnore HttpServletResponse response) throws IOException {
        RestControllerUtil.runRunnable(() -> degreeProgramService.delete(degreeProgramId), response, LOG);
    }
}
