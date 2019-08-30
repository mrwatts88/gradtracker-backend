package edu.uwm.capstone.controller;

import edu.uwm.capstone.util.Concatenation;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("squid:S1075") // suppress sonar warning about hard-coded URL path
public class ConcatenationRestController {

    public static final String CONCATENATE_PATH = "/concatenate/{firstString}/{secondString}";

    /**
     * This endpoint takes two separate values and combines them together.
     *
     * @param firstString  {@link String} value of the first thing to be concatenated
     * @param secondString {@link String} value of the second thing to be concatenated
     * @return {@link String} value of the provided strings concatenated together
     */
    @ApiOperation(value = "Take two separate values and combines them together")
    @GetMapping(value = CONCATENATE_PATH)
    public String concatenate(@PathVariable String firstString, @PathVariable String secondString) {
        return new Concatenation().concatenate(firstString, secondString);
    }

}
