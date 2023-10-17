package com.accountbook.myaccountbook.utils;

import com.accountbook.myaccountbook.persistence.AccountHistory;
import com.accountbook.myaccountbook.repository.AccountHistoryRepository;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;

import java.util.Date;
import java.util.List;

public class SchedulerUtil {

    // AccountHistory에 사용자별 월별 기록 저장한다.
    public static void insertAccountHistory(List<Integer> mids,
                                            IncomeRepository incomeRepository,
                                            ExpenseRepository expenseRepository,
                                            AccountHistoryRepository accountHistoryRepository,
                                            List<AccountHistory> accountHistories,
                                            String year,
                                            String lastMonth,
                                            String fullMonth) {

        // 조회된 사용자 수가 0명이면 아래 로직을 타지 않는다.
        if (!(mids.size() == 0)) {

            // 사용자의 수만큼 반복문 동작한다.
            for (Integer mid : mids) {

                // 지역 변수로 매번 값을 초기화한다.
                int incomeSum = 0;
                int expenseSum = 0;

                // 사용자의 id와 지난 달을 조건으로 수입/지출 금액 List 객체를 조회한다.
                List<Integer> incomes = incomeRepository.findAllIncomeMoney(mid, fullMonth);
                List<Integer> expenses = expenseRepository.findAllExpenseMoney(mid, fullMonth);

                // 등록한 수입 금액 List만큼 반복문을 돌려 수입 총액을 계산한다.
                for (Integer incomeMoney : incomes) {
                    incomeSum += incomeMoney;
                }

                // 등록한 지출 금액 List만큼 반복문을 돌려 지출 총액을 계산한다.
                for (Integer expenseMoney : expenses) {
                    expenseSum += expenseMoney;
                }

                // AccountHistory 객체를 생성한다.
                AccountHistory accountHistory = AccountHistory.builder()
                        .mid(mid)
                        .monthIncome(incomeSum)
                        .monthExpense(expenseSum)
                        .monthSum(incomeSum - expenseSum)
                        .year(year)
                        .month(lastMonth)
                        .regDate(new Date())
                        .build();

                // AccountHistory List 객체에 값을 저장한다.
                accountHistories.add(accountHistory);
            }

            // DB 트랜잭션을 줄이기 위해 반복문 밖에서 일괄 저장한다.
            accountHistoryRepository.saveAll(accountHistories);
        }
    }
}
