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
        flyway(ds);

        return ds;
    }

    @Bean
    @Primary
    @Override
    public Flyway flyway( DataSource dataSource) {
        LOGGER.info("Running database migration on {}", dbDriverUrl);
        Flyway flyway = new Flyway(Flyway.configure()
                .locations(dbMigrationLocation.split("\\s*,\\s*"))
                .outOfOrder(true)
                .dataSource(dataSource));
        flyway.clean(); // needed for unit and integration tests
        flyway.migrate();

        return flyway;
    }

}
