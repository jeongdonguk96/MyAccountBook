package com.accountbook.myaccountbook.util;

import com.accountbook.myaccountbook.constant.MessageConstants;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Service
public class AccountBookUtil {

    // 현재 연도 계산
    public String getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        return formatter.format(new Date());
    }


    // 현재 달 계산
    public String getMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());

        return date.substring(4, 6);
    }


    // 현재 달의 일수 계산
    public String getDays() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));

        LocalDate findMonth = LocalDate.of(year, month, 1);

        return String.valueOf(findMonth.lengthOfMonth());
    }


    // 랜덤 문구 추첨
    public String getRandomMessage() {
        int index = new Random().nextInt(MessageConstants.MESSAGE.length);

        return MessageConstants.MESSAGE[index];
    }
}
