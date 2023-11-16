package com.accountbook.myaccountbook.utils;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Service
public class DateUtil {

    // 현재 연도를 계산한다.
    public static String getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        return formatter.format(new Date());
    }


    // 지난 연도를 계산한다.
    public static String getLastYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        return formatter.format(calendar.getTime());
    }


    // 현재 달을 계산한다.
    public static String getMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM");

        return formatter.format(new Date());
    }


    // 지난 달을 계산한다.
    public static String getLastMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        return formatter.format(calendar.getTime());
    }


    // 현재 달의 일수를 계산한다.
    public static String getDays() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        LocalDate findMonth = LocalDate.of(year, month, 1);

        return String.valueOf(findMonth.lengthOfMonth());
    }


    // 오늘 날짜를 계산한다.
    public static String getToday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();

        return formatter.format(calendar.getTime());
    }


    // 어제 날짜를 계산한다.
    public static String getYesterday() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        return formatter.format(calendar.getTime());
    }
}
