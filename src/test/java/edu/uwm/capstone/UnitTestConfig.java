package edu.uwm.capstone;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

@Configuration
@ConfigurationProperties(prefix = "service")
@EnableAutoConfiguration
@PropertySources({ @PropertySource("classpath:application.properties"), @PropertySource("classpath:test.properties") })
public class UnitTestConfig extends ApplicationConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitTestConfig.class);

    @Bean
    DocumentationPluginsBootstrapper documentationPluginsBootstrapper() {
        return null;
    }

    @Bean
    WebMvcRequestHandlerProvider webMvcRequestHandlerProvider() {
        return null;
    }

    @Override
    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource ds = new DataSource();
        ds.setDriverClassName(dbDriverClassName);
        ds.setUrl(dbDriverUrl);
        ds.setUsername(dbUsername);
        ds.setPassword(dbPassword);

        LOGGER.info("Running database migration on {}", dbDriverUrl);
        Flyway flyway = new Flyway();
        flyway.setLocations(dbMigrationLocation.split("\\s*,\\s*"));
        flyway.setOutOfOrder(true);
        flyway.setDataSource(ds);
        flyway.clean(); // needed for unit and integration tests
        flyway.migrate();

        return ds;
    }

}
