package edu.uwm.capstone.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class SwaggerController {

    static final String SWAGGER_UI = "/swaggerui";
    static final String CONTROLLER_DOCS = "redirect:swagger-ui.html?url=/v2/api-docs/";

    @GetMapping(value = SWAGGER_UI)
    @ApiOperation(value = "Redirects to Swagger-UI")
    public String swaggerui() {
        return CONTROLLER_DOCS;
    }

}
