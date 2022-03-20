package com.demo.java.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.*;

public class Test1 {
    public static void main(String[] args) throws ParseException {
        System.out.println(Instant.now().getEpochSecond());
        System.out.println(Instant.now().toEpochMilli());
        System.out.println(ZonedDateTime.now());
        System.out.println(LocalDateTime.now());
        System.out.println(OffsetDateTime.now());
        System.out.println(Clock.systemUTC());

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime.toLocalDateTime());
        System.out.println(zonedDateTime.toOffsetDateTime());

        String str = zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println(str); // 2022-03-12T22:28:55.226521109+08:00
        ZonedDateTime zTime = ZonedDateTime.parse(str, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        System.out.println(zTime);

        String dateTime = "2020-03-12T22:44:22.123+0900";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nZ");
        ZoneId zoneId = ZonedDateTime.parse(dateTime, formatter).getZone();
        System.out.println(zoneId);

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SZ");
        System.out.println(sdf.parse(dateTime).toInstant());



    }
}
