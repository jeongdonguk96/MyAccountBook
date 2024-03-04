package com.accountbook.myaccountbook.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DateUtilTest {

    @Test
//    @Disabled
    void getYear() {
        // given
        String year = DateUtil.getYear();

        // when

        // then
        assertThat(year).isEqualTo("2023");
        System.out.println("year = " + year);
    }

    @Test
//    @Disabled
    void getLastYear() {
        // given
        String lastYear = DateUtil.getLastYear();

        // when


        // then
        assertThat(lastYear).isEqualTo("2022");
        System.out.println("lastYear = " + lastYear);
    }

    @Test
//    @Disabled
    void getMonth() {
        // given
        String month = DateUtil.getMonth();

        // when


        // then
        assertThat(month).isEqualTo("11");
        System.out.println("month = " + month);
    }

    @Test
//    @Disabled
    void getLastMonth() {
        // given
        String lastMonth = DateUtil.getLastMonth();

        // when


        // then
        assertThat(lastMonth).isEqualTo("10");
        System.out.println("lastMonth = " + lastMonth);
    }

    @Test
//    @Disabled
    void getDays() {
        // given
        String days = DateUtil.getDays();

        // when


        // then
        assertThat(days).isEqualTo("30");
    }

    @Test
//    @Disabled
    void getToday() {
        // given
        String today = DateUtil.getToday();

        // when


        // then
        assertThat(today).isEqualTo("20231116");
    }


    @Test
//    @Disabled
    void getYesterday() {
        // given
        String yesterday = DateUtil.getYesterday();

        // when


        // then
        assertThat(yesterday).isEqualTo("20231115");
    }
}