package edu.uwm.capstone.sql.statement;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SqlStatementsFileParseExceptionUnitTest {

    @Test
    public void verify() {
        String randomString = RandomStringUtils.randomAlphanumeric(100);
        SqlStatementsFileParseException sqlStatementsFileParseException = new SqlStatementsFileParseException(randomString);
        assertTrue(sqlStatementsFileParseException.getMessage().contains(randomString));
    }

}
