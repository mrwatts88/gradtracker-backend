package edu.uwm.capstone.sql.dao;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import edu.uwm.capstone.sql.statement.ISqlStatementsFileLoader;

public abstract class BaseDao<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    protected DataSource dataSource;
    protected NamedParameterJdbcTemplate jdbcTemplate;
    protected ISqlStatementsFileLoader sqlStatementsFileLoader;
    protected BaseRowMapper rowMapper;

    public abstract T create(T object);

    public abstract T read(long id);

    public abstract void update(T object);

    public abstract void delete(long id);

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void setSqlStatementsFileLoader(ISqlStatementsFileLoader sqlStatementsFileLoader) {
        this.sqlStatementsFileLoader = sqlStatementsFileLoader;
    }

    public String sql(String statementName) {
        String statement = this.sqlStatementsFileLoader.sql(statementName);
        LOGGER.trace("Returning statement '{}': {}", statementName, statement);
        return statement;
    }

    public void setRowMapper(BaseRowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

}
