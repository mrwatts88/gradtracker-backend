package edu.uwm.capstone.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class SwaggerController {

    public static final String SWAGGER_UI = "/swaggerui";
    public static final String CONTROLLER_DOCS = "redirect:swagger-ui.html?url=/v2/api-docs/";

    @RequestMapping(value = SWAGGER_UI, method = RequestMethod.GET)
    @ApiOperation(value = "Redirects to Swagger-UI")
    public String swaggerui() {
        return CONTROLLER_DOCS;
    }

}
