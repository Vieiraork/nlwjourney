package com.vieiraork.planner.Helper;

import lombok.experimental.Helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helpers {
    /**
     * Parse string date to LocalDateTime date
     * @param date
     * @return
     */
    public static LocalDateTime transformStringToLocalDatetime(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Return true if firstDate is below or equal the secondDate
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static Boolean compareTwoDatesInterval(LocalDateTime firstDate, LocalDateTime secondDate) {
        return firstDate.isAfter(secondDate) || firstDate.isEqual(secondDate);
    }
}
