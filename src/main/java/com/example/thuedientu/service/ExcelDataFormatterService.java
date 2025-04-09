package com.example.thuedientu.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ExcelDataFormatterService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelDataFormatterService.class);


    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

    public BigDecimal parseBigDecimal(String input,int i) {

        if (input == null || input.trim().isEmpty()) return null;
        try {
            return new BigDecimal(input.replace(",", "").trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid BigDecimar: {}", input, e); // log rõ ràng và chuyên nghiệp
            logger.warn("Invalid BigDecimar: {}", i, e); // log rõ ràng và chuyên nghiệp


            return null;
        }
    }

    public Long parseLong(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try {
            return Long.parseLong(input.trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid Long: " + input);
            return null;
        }
    }

    public Integer parseInteger(String value,int i) {
        if (value == null || value.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            System.out.println(value);
            logger.warn("Invalid Integer: {}", value, e); // log rõ ràng và chuyên nghiệp
            logger.warn("Invalid Integer iiiii: {}", i, e); // log rõ ràng và chuyên nghiệp


            return null; // hoặc throw nếu bạn muốn xử lý ở tầng cao hơn
        }
    }

//    public Integer parseInteger(String input) {
//        if (input == null || input.trim().isEmpty()) return null;
//        try {
//            return Integer.parseInt(input.trim());
//        } catch (NumberFormatException e) {
//            System.err.println("Invalid Integer: " + input);
//            return null;
//        }
//    }

    public Date parseSqlDate(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try {
            LocalDate date = LocalDate.parse(input.trim(), dateFormatter);
            return Date.valueOf(date);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + input);
            return null;
        }
    }

    public Time parseSqlTime(String input) {
        if (input == null || input.trim().isEmpty()) return null;
        try {
            LocalTime time = LocalTime.parse(input.trim(), timeFormatter);
            return Time.valueOf(time);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid time format: " + input);
            return null;
        }
    }

    public static boolean isNumericForSqlServer(String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        try {
            String normalized = str.trim().replace(",", ".");
            new BigDecimal(normalized);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
