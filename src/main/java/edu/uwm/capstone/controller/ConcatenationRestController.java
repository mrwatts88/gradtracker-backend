package edu.uwm.capstone.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.uwm.capstone.util.Concatenation;
import io.swagger.annotations.ApiOperation;

@RestController
public class ConcatenationRestController {

    public static final String CONCATENATE_PATH = "/concatenate/{firstString}/{secondString}";

    /**
     * This endpoint takes two separate values and combines them together.
     * @param firstString {@link String} value of the first thing to be concatenated
     * @param secondString {@link String} value of the second thing to be concatenated
     * @return {@link String} value of the provided strings concatenated together
     */
    @ApiOperation(value = "Take two separate values and combines them together")
    @RequestMapping(value = CONCATENATE_PATH, method = RequestMethod.GET)
    public String concatenate(@PathVariable String firstString, @PathVariable String secondString) {
        return new Concatenation().concatenate(firstString, secondString);
    }

}
