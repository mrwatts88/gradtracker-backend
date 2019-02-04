package edu.uwm.capstone.sql.dao;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

public abstract class BaseRowMapper<T> implements RowMapper<T>, BaseRowReverseMapper<T> {

    public enum BaseColumnType {
        ID(),
        CREATED_DATE(),
        UPDATED_DATE();

        private String columnName;

        BaseColumnType(){
            this.columnName = name().toLowerCase();
        }

        public String getColumnName(){
            return columnName;
        }
    }

    public static final int STANDARD_TRUNCATION_LENGTH = 255;

    /**
     * Convert a {@link LocalDateTime} object's value into milliseconds.
     * @param localDateTime {@link LocalDateTime}
     * @return Long representation of the provided {@link LocalDateTime} in milliseconds.
     */
    public static Long javaTimeFromDate(LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Convert the provided milliseconds value into a {@link LocalDateTime} object.
     * @param javaTime Object milliseconds
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime dateFromJavaTime(Object javaTime) {
        return javaTime == null || javaTime.equals((long) 0) ? null : Instant.ofEpochMilli((Long) javaTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Convert the provided {@link LocalDateTime} into a {@link Timestamp} object.
     * @param localDateTime {@link LocalDateTime}
     * @return {@link Timestamp}
     */
    public static Timestamp localDateTimeToSqlTimestamp(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        } else {
            return Timestamp.valueOf(localDateTime);
        }
    }

    /**
     * Convert the provided {@link Timestamp} into a {@link LocalDateTime} object.
     * @param timestamp {@link Timestamp}
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime sqlTimestampToLocalDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return timestamp.toLocalDateTime();
        }
    }

    /**
     * Convert the provided {@link LocalDate} into a {@link Timestamp} object.
     * @param localDate {@link LocalDate}
     * @return {@link Timestamp}
     */
    public static Timestamp localDateToSqlTimestamp(LocalDate localDate) {
        if (localDate == null) {
            return null;
        } else {
            return Timestamp.valueOf(localDate.atStartOfDay());
        }
    }

    /**
     * Convert the provided {@link Timestamp} into a {@link LocalDate} object.
     * @param timestamp {@link Timestamp}
     * @return {@link LocalDate}
     */
    public static LocalDate sqlTimestampToLocalDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        } else {
            return timestamp.toLocalDateTime().toLocalDate();
        }
    }

    /**
     * Truncate any string that exceeds the length assigned to {@link #STANDARD_TRUNCATION_LENGTH}.
     * @param value String
     * @return String
     */
    public static String truncateString(String value) {
        return StringUtils.abbreviate(value, STANDARD_TRUNCATION_LENGTH);
    }

}
