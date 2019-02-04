package edu.uwm.capstone.sql.statement;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SqlStatementsFileResourceExceptionUnitTest {

    @Test
    public void verifyMessage() {
        String randomString = RandomStringUtils.randomAlphanumeric(100);
        SqlStatementsFileResourceException sqlStatementsFileResourceException = new SqlStatementsFileResourceException(randomString);
        assertTrue(sqlStatementsFileResourceException.getMessage().contains(randomString));
    }

    @Test
    public void verifyMessageAndThrowable() {
        String randomString1 = RandomStringUtils.randomAlphanumeric(100);
        String randomString2 = RandomStringUtils.randomAlphanumeric(120);
        Throwable throwable = new Throwable(randomString2);
        SqlStatementsFileResourceException sqlStatementsFileResourceException = new SqlStatementsFileResourceException(randomString1, throwable);
        assertTrue(sqlStatementsFileResourceException.getMessage().contains(randomString1));
        assertTrue(sqlStatementsFileResourceException.getMessage().contains(randomString2));
    }

}
