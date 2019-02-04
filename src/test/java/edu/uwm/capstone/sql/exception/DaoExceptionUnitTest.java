package edu.uwm.capstone.sql.exception;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class DaoExceptionUnitTest {

    @Test
    public void errorCode() {
        DaoException daoException = new DaoException();
        assertNull(daoException.getMessage());
        assertNull(daoException.getLocalizedMessage());
        assertNull(daoException.getCause());
    }

    @Test
    public void errorCodeAndMessage() {
        String message = RandomStringUtils.randomAlphanumeric(100);
        DaoException daoException = new DaoException(message);
        assertEquals(message, daoException.getMessage());
        assertEquals(message, daoException.getLocalizedMessage());
        assertNull(daoException.getCause());
    }

    @Test
    public void errorCodeAndThrowable() {
        String declaringClass = RandomStringUtils.randomAlphanumeric(30);
        String methodName = RandomStringUtils.randomAlphanumeric(28);
        String fileName = RandomStringUtils.randomAlphanumeric(26);
        int lineNumber = new Random().ints(10, 1000).findAny().getAsInt();
        StackTraceElement stackTraceElement = new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
        StackTraceElement[] stackTraceElements = { stackTraceElement };

        String throwableMessage = RandomStringUtils.randomAlphanumeric(20);
        Throwable throwable = new Throwable(throwableMessage);
        throwable.setStackTrace(stackTraceElements);

        DaoException daoException = new DaoException(throwable);
        assertTrue(daoException.getMessage().contains(throwableMessage));
        assertTrue(daoException.getLocalizedMessage().contains(throwableMessage));
        assertNotNull(daoException.getCause());
        assertEquals(throwableMessage, daoException.getCause().getMessage());
        assertEquals(1, daoException.getCause().getStackTrace().length);

        List<StackTraceElement> validateStackTraceElements = Arrays.asList(daoException.getCause().getStackTrace().clone());
        assertEquals(declaringClass, validateStackTraceElements.get(0).getClassName());
        assertEquals(methodName, validateStackTraceElements.get(0).getMethodName());
        assertEquals(fileName, validateStackTraceElements.get(0).getFileName());
        assertEquals(lineNumber, validateStackTraceElements.get(0).getLineNumber());
    }

    @Test
    public void errorCodeMessageAndThrowable() {
        String message = RandomStringUtils.randomAlphanumeric(100);

        String declaringClass = RandomStringUtils.randomAlphanumeric(30);
        String methodName = RandomStringUtils.randomAlphanumeric(28);
        String fileName = RandomStringUtils.randomAlphanumeric(26);
        int lineNumber = new Random().ints(10, 1000).findAny().getAsInt();
        StackTraceElement stackTraceElement = new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
        StackTraceElement[] stackTraceElements = { stackTraceElement };

        String throwableMessage = RandomStringUtils.randomAlphanumeric(20);
        Throwable throwable = new Throwable(throwableMessage);
        throwable.setStackTrace(stackTraceElements);

        DaoException daoException = new DaoException(message, throwable);
        assertEquals(message, daoException.getMessage());
        assertEquals(message, daoException.getLocalizedMessage());
        assertNotNull(daoException.getCause());
        assertEquals(throwableMessage, daoException.getCause().getMessage());
        assertEquals(1, daoException.getCause().getStackTrace().length);

        List<StackTraceElement> validateStackTraceElements = Arrays.asList(daoException.getCause().getStackTrace().clone());
        assertEquals(declaringClass, validateStackTraceElements.get(0).getClassName());
        assertEquals(methodName, validateStackTraceElements.get(0).getMethodName());
        assertEquals(fileName, validateStackTraceElements.get(0).getFileName());
        assertEquals(lineNumber, validateStackTraceElements.get(0).getLineNumber());
    }

}
