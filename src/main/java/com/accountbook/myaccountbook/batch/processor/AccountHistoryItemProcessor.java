package com.accountbook.myaccountbook.batch.processor;

import com.accountbook.myaccountbook.entity.AccountHistory;
import com.accountbook.myaccountbook.entity.Member;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.utils.AccountBookUtil;
import com.accountbook.myaccountbook.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

@RequiredArgsConstructor
public class AccountHistoryItemProcessor implements ItemProcessor<Member, AccountHistory> {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    String year = DateUtil.getYear(); // 현재 연도 (yyyy)
    String lastYear = DateUtil.getLastYear(); // 지난 연도 (yyyy)
    String month = DateUtil.getMonth(); // 현재 달 (MM)
    String lastMonth = DateUtil.getLastMonth(); // 지난 달 (MM)
    String fullMonth = (month.equals("01") ? lastYear : year) + lastMonth; // 지난 달 (yyyyMM)

    @Override
    public AccountHistory process(Member item) {
        int mid = item.getMid();

        // 지난 달의 수입/지출 총액을 계산한다.
        int incomeSum = AccountBookUtil.calculateIncomeSum(incomeRepository, mid, fullMonth);
        int expenseSum = AccountBookUtil.calculateExpenseSum(expenseRepository, mid, fullMonth);

        // AccountHistory 객체를 생성한다.
        return AccountHistory.builder()
                .mid(mid)
                .monthIncome(incomeSum)
                .monthExpense(expenseSum)
                .monthSum(incomeSum - expenseSum)
                .year(year)
                .month(lastMonth)
                .regDate(new Date())
                .build();
    }
}
