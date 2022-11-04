package ru.practicum.explore.with.me;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static ru.practicum.explore.with.me.Constants.formatter;

public class DateTimeEncoder {
    public static String encode(LocalDateTime dateTime) {
        String dateTimeString = dateTime.format(formatter);
        return URLEncoder.encode(dateTimeString, StandardCharsets.UTF_8);
    }

    public static LocalDateTime decodeAndParse(String encodedDateTimeString) {
        String decodedDateTimeString = URLDecoder.decode(encodedDateTimeString, StandardCharsets.UTF_8);
        return LocalDateTime.parse(decodedDateTimeString, formatter);
    }
}
