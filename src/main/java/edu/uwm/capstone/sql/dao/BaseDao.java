package edu.uwm.capstone.sql.dao;

import edu.uwm.capstone.sql.statement.ISqlStatementsFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public abstract class BaseDao<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    protected DataSource dataSource;
    protected NamedParameterJdbcTemplate jdbcTemplate;
    protected ISqlStatementsFileLoader sqlStatementsFileLoader;
    protected BaseRowMapper rowMapper;

    public abstract V create(V object);

    public abstract V read(K id);

    public abstract V update(V object);

    public abstract void delete(K id);

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
