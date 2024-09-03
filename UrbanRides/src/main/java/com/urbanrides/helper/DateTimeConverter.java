package com.urbanrides.helper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DateTimeConverter {



    public static String localTimeToString(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localTime.format(formatter);
    }

    public static LocalTime stringToLocalTime(String timeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(timeString, formatter);
    }

    public static String localDateToString(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    public static LocalDate stringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
    public static String convertTo24HourFormat(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
    public static LocalTime convertStringToLocalTime(String timeString) {
        try {
            LocalTime dropoffTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            return dropoffTime;

        } catch (DateTimeParseException e) {
            return null;
        }
    }
    public static LocalTime convertMinsToLocalTime(String estimatedTime) {
        String[] parts = estimatedTime.split(" ");
        int minutes = Integer.parseInt(parts[0]);
        return LocalTime.of(0, minutes);
    }
    public String formatDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(formatter);
        return formattedDate;
    }

    public static String convertToDayNames(String numOfDays) {
        Map<String, String> dayMap = new HashMap<>();
        dayMap.put("1", "Monday");
        dayMap.put("2", "Tuesday");
        dayMap.put("3", "Wednesday");
        dayMap.put("4", "Thursday");
        dayMap.put("5", "Friday");
        dayMap.put("6", "Saturday");
        dayMap.put("7", "Sunday");
        String[] daysArray = numOfDays.split(",");
        String dailyPickUpDays = java.util.Arrays.stream(daysArray).map(dayMap::get).filter(day -> day != null).collect(Collectors.joining(", "));
        return dailyPickUpDays;
    }

}
