package com.accountbook.myaccountbook.utils;

import com.accountbook.myaccountbook.constant.MessageConstant;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Service
public class AccountBookUtil {

    // 현재 연도 계산
    public static String getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        return formatter.format(new Date());
    }


    // 지난 연도 계산
    public static String getLastYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String date = formatter.format(new Date());
        int intDate = Integer.parseInt(date);

        return String.valueOf(intDate - 1);
    }


    // 현재 달 계산
    public static String getMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());

        return date.substring(4, 6);
    }


    // 지난 달 계산
    public static String getLastMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int intDate = Integer.parseInt(date);
        String stringDate = String.valueOf(intDate - 1);

        return stringDate.substring(4, 6);
    }


    // 현재 달의 일수 계산
    public static String getDays() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));

        LocalDate findMonth = LocalDate.of(year, month, 1);

        return String.valueOf(findMonth.lengthOfMonth());
    }


    // 랜덤 문구 추첨
    public static String getRandomMessage() {
        int index = new Random().nextInt(MessageConstant.MESSAGE.length);

        return MessageConstant.MESSAGE[index];
    }
}
