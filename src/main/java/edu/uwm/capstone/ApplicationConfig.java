package edu.uwm.capstone;

import edu.uwm.capstone.db.*;
import edu.uwm.capstone.sql.statement.ISqlStatementsFileLoader;
import edu.uwm.capstone.sql.statement.SqlStatementsFileLoader;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@ConfigurationProperties(prefix = "service")
@EnableSwagger2
public class ApplicationConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    protected String dbDriverClassName;
    protected String dbDriverUrl;
    protected String dbUsername;
    protected String dbPassword;
    protected String dbMigrationLocation;
    protected int dbPoolMaxWait;
    protected boolean dbPoolRemoveAbandoned;
    protected int dbPoolRemoveAbandonedTimeout;
    protected boolean dbPoolLogAbandoned;
    protected long dbPoolMaxAge;
    protected String sqlStatementsResourceLocation;
    protected DataSource ds;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        if (ds == null) {
            LOGGER.info("Loading DataSource");
            PoolProperties poolProperties = new PoolProperties();
            poolProperties.setDriverClassName(dbDriverClassName);
            poolProperties.setUrl(dbDriverUrl);
            poolProperties.setUsername(dbUsername);
            poolProperties.setPassword(dbPassword);
            poolProperties.setTestOnBorrow(true);
            poolProperties.setValidationQuery("SELECT 1");

            // Set additional pool properties
            poolProperties.setMaxWait(dbPoolMaxWait);
            poolProperties.setRemoveAbandoned(dbPoolRemoveAbandoned);
            poolProperties.setRemoveAbandonedTimeout(dbPoolRemoveAbandonedTimeout);
            poolProperties.setLogAbandoned(dbPoolLogAbandoned);
            poolProperties.setMaxAge(dbPoolMaxAge);
            poolProperties.setMaxActive(600);

            ds = new DataSource();
            ds.setPoolProperties(poolProperties);
            flyway(ds);
        }

        return ds;
    }

    @Bean
    @Primary
    public Flyway flyway(DataSource dataSource) {
        LOGGER.info("Running database migration on {}", dbDriverUrl);
        Flyway flyway = new Flyway(Flyway.configure()
                .locations(dbMigrationLocation.split("\\s*,\\s*"))
                .outOfOrder(true)
                .dataSource(dataSource));
        flyway.repair();
        flyway.migrate();

        return flyway;
    }

    @Bean
    public ISqlStatementsFileLoader sqlStatementsFileLoader() {
        SqlStatementsFileLoader loader = new SqlStatementsFileLoader();
        loader.setStatementResourceLocation(sqlStatementsResourceLocation);
        return loader;
    }

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        userDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        userDao.setRowMapper(userDaoRowMapper());
        return userDao;
    }

    @Bean
    public RoleDao roleDao() {
        RoleDao roleDao = new RoleDao();
        roleDao.setDataSource(dataSource());
        roleDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        roleDao.setRowMapper(roleDaoRowMapper());
        return roleDao;
    }

    @Bean
    public FormDefinitionDao formDefinitionDao() {
        FormDefinitionDao formDefinitionDao = new FormDefinitionDao();
        formDefinitionDao.setDataSource(dataSource());
        formDefinitionDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        formDefinitionDao.setRowMapper(formDefinitionDaoRowMapper());
        return formDefinitionDao;
    }

    @Bean
    public FieldDefinitionDao fieldDefinitionDao() {
        FieldDefinitionDao fieldDefinitionDao = new FieldDefinitionDao();
        fieldDefinitionDao.setDataSource(dataSource());
        fieldDefinitionDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        fieldDefinitionDao.setRowMapper(fieldDefinitionDaoRowMapper());
        return fieldDefinitionDao;
    }

    @Bean
    public FormDao formDao() {
        FormDao formDao = new FormDao();
        formDao.setDataSource(dataSource());
        formDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        formDao.setRowMapper(formDaoRowMapper());
        return formDao;
    }

    @Bean
    public FieldDao fieldDao() {
        FieldDao fieldDao = new FieldDao();
        fieldDao.setDataSource(dataSource());
        fieldDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        fieldDao.setRowMapper(fieldDaoRowMapper());
        return fieldDao;
    }

    @Bean
    public DegreeProgramDao degreeProgramDao() {
        DegreeProgramDao degreeProgramDao = new DegreeProgramDao();
        degreeProgramDao.setDataSource(dataSource());
        degreeProgramDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        degreeProgramDao.setRowMapper(degreeProgramDaoRowMapper());
        return degreeProgramDao;
    }

    @Bean
    public DegreeProgramStateDao degreeProgramStateDao() {
        DegreeProgramStateDao degreeProgramStateDao = new DegreeProgramStateDao();
        degreeProgramStateDao.setDataSource(dataSource());
        degreeProgramStateDao.setSqlStatementsFileLoader(sqlStatementsFileLoader());
        degreeProgramStateDao.setRowMapper(degreeProgramStateDaoRowMapper());
        return degreeProgramStateDao;
    }

    @Bean
    public UserDaoRowMapper userDaoRowMapper() {
        return new UserDaoRowMapper();
    }

    @Bean
    public RoleDaoRowMapper roleDaoRowMapper() {
        return new RoleDaoRowMapper();
    }

    @Bean
    public FormDefinitionDaoRowMapper formDefinitionDaoRowMapper() {
        return new FormDefinitionDaoRowMapper();
    }

    @Bean
    public FormDaoRowMapper formDaoRowMapper() {return new FormDaoRowMapper();}

    @Bean
    public FieldDaoRowMapper fieldDaoRowMapper() {return new FieldDaoRowMapper();}

    @Bean
    public FieldDefinitionDaoRowMapper fieldDefinitionDaoRowMapper() {
        return new FieldDefinitionDaoRowMapper();
    }

    @Bean
    public DegreeProgramDaoRowMapper degreeProgramDaoRowMapper() {
        return new DegreeProgramDaoRowMapper();
    }

    @Bean
    public DegreeProgramStateDaoRowMapper degreeProgramStateDaoRowMapper() {
        return new DegreeProgramStateDaoRowMapper();
    }

    public String getDbDriverClassName() {
        return dbDriverClassName;
    }

    public void setDbDriverClassName(String dbDriverClassName) {
        this.dbDriverClassName = dbDriverClassName;
    }

    public String getDbDriverUrl() {
        return dbDriverUrl;
    }

    public void setDbDriverUrl(String dbDriverUrl) {
        this.dbDriverUrl = dbDriverUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbMigrationLocation() {
        return dbMigrationLocation;
    }

    public void setDbMigrationLocation(String dbMigrationLocation) {
        this.dbMigrationLocation = dbMigrationLocation;
    }

    public int getDbPoolMaxWait() {
        return dbPoolMaxWait;
    }

    public void setDbPoolMaxWait(int dbPoolMaxWait) {
        this.dbPoolMaxWait = dbPoolMaxWait;
    }

    public boolean isDbPoolRemoveAbandoned() {
        return dbPoolRemoveAbandoned;
    }

    public void setDbPoolRemoveAbandoned(boolean dbPoolRemoveAbandoned) {
        this.dbPoolRemoveAbandoned = dbPoolRemoveAbandoned;
    }

    public int getDbPoolRemoveAbandonedTimeout() {
        return dbPoolRemoveAbandonedTimeout;
    }

    public void setDbPoolRemoveAbandonedTimeout(int dbPoolRemoveAbandonedTimeout) {
        this.dbPoolRemoveAbandonedTimeout = dbPoolRemoveAbandonedTimeout;
    }

    public boolean isDbPoolLogAbandoned() {
        return dbPoolLogAbandoned;
    }

    public void setDbPoolLogAbandoned(boolean dbPoolLogAbandoned) {
        this.dbPoolLogAbandoned = dbPoolLogAbandoned;
    }

    public long getDbPoolMaxAge() {
        return dbPoolMaxAge;
    }

    public void setDbPoolMaxAge(long dbPoolMaxAge) {
        this.dbPoolMaxAge = dbPoolMaxAge;
    }

    public String getSqlStatementsResourceLocation() {
        return sqlStatementsResourceLocation;
    }

    public void setSqlStatementsResourceLocation(String sqlStatementsResourceLocation) {
        this.sqlStatementsResourceLocation = sqlStatementsResourceLocation;
    }
}
