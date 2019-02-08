package edu.uwm.capstone.sql.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public abstract class BaseRowMapper<T> implements RowMapper<T>, BaseRowReverseMapper<T> {

    public enum BaseColumnType {
        ID(),
        CREATED_DATE(),
        UPDATED_DATE();

        private String columnName;

        BaseColumnType() {
            this.columnName = name().toLowerCase();
        }

        public String getColumnName() {
            return columnName;
        }
    }

    public static final int STANDARD_TRUNCATION_LENGTH = 255;

    /**
     * Convert a {@link LocalDateTime} object's value into nanoseconds.
     *
     * @param localDateTime {@link LocalDateTime}
     * @return Long representation of the provided {@link LocalDateTime} in nanoseconds.
     */
    public static Long javaTimeFromDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;

        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return TimeUnit.SECONDS.toNanos(instant.getEpochSecond()) + instant.getNano();
    }

    /**
     * Convert the provided nanoseconds value into a {@link LocalDateTime} object.
     *
     * @param javaTime Object nanoseconds
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime dateFromJavaTime(Object javaTime) {
        if (javaTime == null || javaTime.equals((long) 0)) return null;

        long nanos = (long) javaTime;
        Instant instant = Instant.ofEpochSecond(TimeUnit.NANOSECONDS.toSeconds(nanos), nanos % TimeUnit.SECONDS.toNanos(1));
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Convert the provided {@link LocalDateTime} into a {@link Timestamp} object.
     *
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
     *
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
     *
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
     *
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
     *
     * @param value String
     * @return String
     */
    public static String truncateString(String value) {
        return StringUtils.abbreviate(value, STANDARD_TRUNCATION_LENGTH);
    }

}
