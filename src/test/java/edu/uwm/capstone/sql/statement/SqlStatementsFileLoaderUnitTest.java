package edu.uwm.capstone.sql.statement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SqlStatementsFileLoaderUnitTest.Config.class)
@TestExecutionListeners(value = {DependencyInjectionTestExecutionListener.class})
public class SqlStatementsFileLoaderUnitTest {

    @Configuration
    static class Config {

    }

    @Autowired
    ApplicationContext applicationContext;

    @Before
    public void setUp() {
        assertNotNull(applicationContext);
    }

    /**
     * Verify that {@link SqlStatementsFileLoader#sql(String)} works correctly when a valid
     * {@link SqlStatementsFileLoader#statementResourceLocation} and
     * {@link SqlStatementsFileLoader#setApplicationContext(ApplicationContext)} are provided.
     * This test uses /main/resources/sql/profile.sql as the resource to be parsed.
     */
    @Test
    public void sqlWithStatementResourceLocation() {
        SqlStatementsFileLoader sqlStatementsFileLoader = new SqlStatementsFileLoader();
        sqlStatementsFileLoader.setStatementResourceLocation("classpath*:*/*.sql");
        sqlStatementsFileLoader.setApplicationContext(applicationContext);

        assertNotNull(sqlStatementsFileLoader.getStatementResourceLocation());
        assertNotNull(sqlStatementsFileLoader.sql("createProfile"));
        assertNotNull(sqlStatementsFileLoader.sql("readProfile"));
        assertNotNull(sqlStatementsFileLoader.sql("updateProfile"));
        assertNotNull(sqlStatementsFileLoader.sql("deleteProfile"));
    }

    /**
     * Verify that {@link SqlStatementsFileLoader#sql(String)} throws {@link SqlStatementsMissingException}
     * when {@link SqlStatementsFileLoader#statementResourceLocation} and
     * {@link SqlStatementsFileLoader#setApplicationContext(ApplicationContext)} are not correctly provided.
     */
    @Test(expected = SqlStatementsMissingException.class)
    public void sqlWithoutStatementResourceLocation() {
        SqlStatementsFileLoader sqlStatementsFileLoader = new SqlStatementsFileLoader();
        assertNotNull(sqlStatementsFileLoader.sql(""));
    }

}
