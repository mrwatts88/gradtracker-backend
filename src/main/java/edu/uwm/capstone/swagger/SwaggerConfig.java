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
                "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJmaXJzdE5hbWVcIjpcImRlZmF1bHRfZmlyc3RfbmFtZVwiLFwibGFzdE5hbWVcIjpcImRlZmF1bHRfbGFzdF9uYW1lXCIsXCJwYW50aGVySWRcIjpcIjEyMzQ1Njc4OVwiLFwiZW1haWxcIjpcImRlZmF1bHRAdXdtLmVkdVwiLFwiZW5hYmxlZFwiOnRydWUsXCJyb2xlTmFtZXNcIjpbXSxcImF1dGhvcml0aWVzXCI6W1wiVEVTVFwiXX0iLCJleHAiOjE1NzM2MTk0MDV9.P9Nr3tt0w6gxnzNgoDSMYXWxEgHDLiNu82lAFyUVbJnWUMzX3Ymoaunmq5wBpl3x0_gjIg3NITr1X0XPNpFm5g",
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
