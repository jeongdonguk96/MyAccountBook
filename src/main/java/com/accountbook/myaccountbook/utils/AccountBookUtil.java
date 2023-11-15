package com.accountbook.myaccountbook.utils;

import com.accountbook.myaccountbook.constant.MessageConstant;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AccountBookUtil {

    // 현재 연도를 계산한다.
    public static String getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        return formatter.format(new Date());
    }


    // 지난 연도를 계산한다.
    public static String getLastYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String date = formatter.format(new Date());
        int intDate = Integer.parseInt(date);

        return String.valueOf(intDate - 1);
    }


    // 현재 달을 계산한다.
    public static String getMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());

        return date.substring(4, 6);
    }


    // 지난 달을 계산한다.
    public static String getLastMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int intDate = Integer.parseInt(date);
        String stringDate = String.valueOf(intDate - 1);

        return stringDate.substring(4, 6);
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


    // 랜덤 문구를 추첨한다.
    public static String getRandomMessage() {
        int index = new Random().nextInt(MessageConstant.MESSAGE.length);

        return MessageConstant.MESSAGE[index];
    }


    // 지난 달의 수입 총액을 계산한다.
    public static int calculateIncomeSum(IncomeRepository incomeRepository, int mid, String fullMonth) {
        // 지역 변수로 매번 값을 초기화한다.
        int incomeSum = 0;

        // 사용자의 id와 지난 달을 조건으로 수입 금액 List 객체를 조회한다.
        List<Integer> incomes = incomeRepository.findAllIncomeMoney(mid, fullMonth);

        // 등록한 수입 금액 List만큼 반복문을 돌려 지출 총액을 계산한다.
        for (Integer incomeMoney : incomes) {
            incomeSum += incomeMoney;
        }

        return incomeSum;
    }


    // 지난 달의 지출 총액을 계산한다.
    public static int calculateExpenseSum(ExpenseRepository expenseRepository, int mid, String fullMonth) {
        // 지역 변수로 매번 값을 초기화한다.
        int expenseSum = 0;

        // 사용자의 id와 지난 달을 조건으로 지출 금액 List 객체를 조회한다.
        List<Integer> expenses = expenseRepository.findAllExpenseMoney(mid, fullMonth);

        // 등록한 지출 금액 List만큼 반복문을 돌려 지출 총액을 계산한다.
        for (Integer expenseMoney : expenses) {
            expenseSum += expenseMoney;
        }

        return expenseSum;
    }
}
