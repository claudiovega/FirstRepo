package com.examples.localTime;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class LocalTimeExample {

    public static void main(String[] args){
        ZoneId zoneId = ZoneId.of("Chile/Continental");

        LocalDate localDate = LocalDate.now(zoneId);

        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        LocalTime localTime  = LocalTime.now(zoneId);
        System.out.println(localDate);
        System.out.println(localTime);
        System.out.println(localDateTime);

/*        DateTimeFormatter oldPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("#########################");
        LocalDateTime datetime = LocalDateTime.parse(localDate.toString(), oldPattern);
        System.out.println(datetime);
        String output = datetime.format(newPattern);
        System.out.println(output);*/
        DateTimeFormatter newPattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println(localDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedString = localDate.format(formatter);
        System.out.println(formattedString);
//        Read more: https://www.java67.com/2018/01/how-to-change-date-format-of-string-in-java8.html#ixzz6KLgD6x3H

//        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();

//        Collection<String> availableZoneIdsList = new ArrayList<>();
//        availableZoneIdsList.addAll(availableZoneIds);
//        List<String> sortedList = availableZoneIdsList.stream().sorted().collect(Collectors.toList());
//        sortedList.forEach(System.out::println);


    }
}
