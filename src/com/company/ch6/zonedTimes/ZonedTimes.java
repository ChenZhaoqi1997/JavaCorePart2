package com.company.ch6.zonedTimes;

import java.time.*;

public class ZonedTimes{
    public static void main(String[] args) {
        ZonedDateTime apollo11launch = ZonedDateTime.of(1969, 7, 16, 9, 32, 0, 0,
                ZoneId.of("America/New_York"));
        System.out.println(apollo11launch);
        Instant instant = apollo11launch.toInstant();
        System.out.println(instant);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));
        System.out.println(zonedDateTime);
        ZonedDateTime skipped = ZonedDateTime.of(LocalDate.of(2013, 10, 27),
                LocalTime.of(2, 30),
                ZoneId.of("Europe/Berlin"));
        System.out.println(skipped);
    }
}
