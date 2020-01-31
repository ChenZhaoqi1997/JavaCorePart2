package com.company.ch6.localdates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class LocalDates {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println(today);
        LocalDate a = LocalDate.of(1903, 6, 14);
        System.out.println(a);
        a = LocalDate.of(1903, Month.JUNE, 14);
        System.out.println(a);
        LocalDate b = LocalDate.of(2018, 1, 1).plusDays(255);
        System.out.println(b);
        LocalDate c = LocalDate.of(2018, Month.JULY, 4);
        LocalDate d = LocalDate.of(2018, Month.DECEMBER, 25);
        System.out.println(c.until(d));
        System.out.println(c.until(d, ChronoUnit.DAYS));
        System.out.println(LocalDate.of(2016, 1, 31).plusMonths(1));
        System.out.println(LocalDate.of(2016, 3, 31).minusMonths(1));
        DayOfWeek startOfLastMillennium = LocalDate.of(1900, 1, 1).getDayOfWeek();
        System.out.println(startOfLastMillennium);
        System.out.println(startOfLastMillennium.getValue());
        System.out.println(DayOfWeek.SATURDAY.plus(3));
    }
}
