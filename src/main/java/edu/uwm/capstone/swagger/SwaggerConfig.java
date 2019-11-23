package edu.uwm.capstone.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfig {

    private String description;
    private String title;
    private String version;
    private String host;

    @Bean
    public SecurityConfiguration security() {
        return new SecurityConfiguration(null, // "client id",
                null, // "client secret",
                null, // "realm",
                null, // "app",
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJpZFwiOjE1MCxcImZpcnN0TmFtZVwiOlwiZGVmYXVsdF9maXJzdF9uYW1lXCIsXCJsYXN0TmFtZVwiOlwiZGVmYXVsdF9sYXN0X25hbWVcIixcInBhbnRoZXJJZFwiOlwiMTIzNDU2Nzg5XCIsXCJlbWFpbFwiOlwiZGVmYXVsdEB1d20uZWR1XCIsXCJyb2xlTmFtZXNcIjpbXCJBZG1pblwiXSxcImF1dGhvcml0aWVzXCI6W1wiQ1JFQVRFX1VTRVJcIixcIlJFQURfQUxMX0ZPUk1TXCIsXCJERUxFVEVfVVNFUlwiLFwiQ1JFQVRFX1JPTEVcIixcIlVQREFURV9ST0xFXCIsXCJERUxFVEVfUk9MRVwiLFwiQVBQUk9WRV9GT1JNXCIsXCJVUERBVEVfVVNFUlwiLFwiQ1JFQVRFX0ZPUk1fREVGXCIsXCJSRUFEX0FMTF9VU0VSU1wiLFwiVVBEQVRFX0ZPUk1fREVGXCIsXCJERUxFVEVfRk9STV9ERUZcIl0sXCJlbmFibGVkXCI6dHJ1ZX0iLCJleHAiOjE1NzU0MDE3NjN9.vBOprN3tg_GlWWg_N0JkHaVeffyxYdGafwALjJ13kchhqbBeyWtMTjF3laYepQhtq6IDRGMVxSIVkWd2Eg4a4w\n",
                ApiKeyVehicle.HEADER, "Authorization", "," /* scope separator */);
    }

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .paths(paths())
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
                .build()
                .host(host)
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON.toString(), MediaType.TEXT_PLAIN.toString()))
                .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON.toString()))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version(version)
                .description(description)
                .title(title).build();
    }

    private Predicate<String> paths() {
        return or(
                regex("/" + ".*"),
                regex("/admin" + ".*"));
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
