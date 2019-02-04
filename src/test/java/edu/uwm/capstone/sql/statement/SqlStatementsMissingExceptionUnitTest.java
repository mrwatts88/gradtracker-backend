package edu.uwm.capstone.sql.statement;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SqlStatementsMissingExceptionUnitTest {

    @Test
    public void verify() {
        String randomString = RandomStringUtils.randomAlphanumeric(100);
        SqlStatementsMissingException sqlStatementsMissingException = new SqlStatementsMissingException(randomString);
        assertTrue(sqlStatementsMissingException.getMessage().contains(randomString));
    }

}
