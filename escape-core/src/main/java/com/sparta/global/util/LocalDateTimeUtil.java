package com.sparta.global.util;

import com.sparta.global.exception.customException.LocalDateTimeException;
import com.sparta.global.exception.errorCode.LocalDateTimeErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class LocalDateTimeUtil {

  // 기본 Formatter
  private final static DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm");

  private LocalDateTimeUtil() {
  }

  public static LocalDateTime parseDateTimeStringToLocalDateTime(String dateTimeString) {
    try {
      LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, defaultFormatter);
      checkValidTime(localDateTime);
      return localDateTime;
    } catch (DateTimeParseException e) {
      throw new LocalDateTimeException(LocalDateTimeErrorCode.DATETIME_PARSE_ERROR);
    }
  }

  public static LocalDate parseDateStringToLocalDate(String dateString) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      return LocalDate.parse(dateString, formatter);
    } catch (DateTimeParseException e) {
      throw new LocalDateTimeException(LocalDateTimeErrorCode.DATETIME_PARSE_ERROR);
    }
  }

  public static String parseLocalDateTimeToString(LocalDateTime localDateTime) {
    try {
      return localDateTime.format(defaultFormatter);
    } catch (DateTimeParseException e) {
      throw new LocalDateTimeException(LocalDateTimeErrorCode.DATETIME_PARSE_ERROR);
    }
  }

  public static void checkValidTime(LocalDateTime localDateTime) {
    if (localDateTime.isBefore(LocalDateTime.now())) {
      throw new LocalDateTimeException(LocalDateTimeErrorCode.INVALID_PAST_TIME);
    }
  }

  public static LocalDateTime calculateEndTime(LocalDateTime startTime, Integer duration) {
    return startTime.plusMinutes(duration);
  }
}
