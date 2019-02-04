package edu.uwm.capstone.sql.dao;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class BaseRowMapperUnitTest {

    @Test
    public void baseColumnType() {
        assertNotNull(BaseRowMapper.BaseColumnType.values());
        for (BaseRowMapper.BaseColumnType baseColumnType : BaseRowMapper.BaseColumnType.values()) {
            assertNotNull(baseColumnType.getColumnName());
        }
    }

    @Test
    public void javaTimeFromDate() {
        LocalDateTime now = LocalDateTime.now();
        Long nowAsLong = BaseRowMapper.javaTimeFromDate(now);
        LocalDateTime nowFromLong = BaseRowMapper.dateFromJavaTime(nowAsLong);
        assertEquals(now, nowFromLong);
    }

    @Test
    public void localDateTimeToSqlTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = BaseRowMapper.localDateTimeToSqlTimestamp(now);
        LocalDateTime nowFromTimestamp = BaseRowMapper.sqlTimestampToLocalDateTime(timestamp);
        assertEquals(now, nowFromTimestamp);
    }

    @Test
    public void localDateToSqlTimestamp() {
        LocalDate now = LocalDate.now();
        Timestamp timestamp = BaseRowMapper.localDateToSqlTimestamp(now);
        LocalDate nowFromTimestamp = BaseRowMapper.sqlTimestampToLocalDate(timestamp);
        assertEquals(now, nowFromTimestamp);
    }

    @Test
    public void truncateString() {
        String value = RandomStringUtils.randomAlphabetic(BaseRowMapper.STANDARD_TRUNCATION_LENGTH);
        String truncatedValue = BaseRowMapper.truncateString(value);
        assertEquals(value, truncatedValue);

        value = RandomStringUtils.randomAlphabetic(BaseRowMapper.STANDARD_TRUNCATION_LENGTH + 1);
        truncatedValue = BaseRowMapper.truncateString(value);
        assertNotEquals(value, truncatedValue);
        assertTrue(truncatedValue.endsWith("..."));
    }

}
