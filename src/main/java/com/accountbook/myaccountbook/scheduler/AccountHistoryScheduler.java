package com.accountbook.myaccountbook.scheduler;

import com.accountbook.myaccountbook.domain.AccountHistory;
import com.accountbook.myaccountbook.repository.AccountHistoryRepository;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountHistoryScheduler {

    private final MemberRepository memberRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final AccountHistoryRepository accountHistoryRepository;


    /**
     * 매달 1일 0시 0분 0초에 동작하는 스케줄러
     * 지난 달의 수입/지출 금액을 토대로
     * AccountHistory 엔티티에 값을 저장
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void insertAccountHistory() {
        String year = getYear(); // 현재 년도
        String lastYear = getLastYear(); // 지난 년도
        String month = getMonth(); // 현재 달
        String lastMonth = month.equals("1") ? "12": getLastMonth(); // 지난 달
        String fullMonth = (month.equals("1") ? lastYear : year) + lastMonth; // 지난 달 yyyyMM

        log.info("========== {}년 {}월 1일 0시 0분 0초 진입 ==========", year, month);
        log.info("========== AccountHistoryScheduler 동작 START ==========");
        log.info("AccountHistoryScheduler Start Time = {}", LocalDateTime.now());

        // 빈 AccountHistory List 객체 생성
        List<AccountHistory> accountHistories = new ArrayList<>();

        // 현재 전체 사용자의 mid값을 리스트로 받음
        List<Integer> mids = memberRepository.findAllMemberMid();
        log.info("조회된 사용자 수 = {}", mids.size());

        // 조회된 사용자 수가 0명이면 아래 로직 동작 X
        if (!(mids.size() == 0)) {

            // 사용자의 수만큼 반복문 동작
            for (Integer mid : mids) {

                // 지역 변수로 매번 값 초기화
                int incomeSum = 0;
                int expenseSum = 0;

                // 사용자의 id와 지난 달을 조건으로 수입/지출 금액 List 객체 조회
                List<Integer> incomes = incomeRepository.findAllIncomeMoney(mid, fullMonth);
                List<Integer> expenses = expenseRepository.findAllExpenseMoney(mid, fullMonth);

                // 등록한 수입 금액 List만큼 반복문을 돌려 수입 총액 계산
                for (Integer incomeMoney : incomes) {
                    incomeSum += incomeMoney;
                }

                // 등록한 지출 금액 List만큼 반복문을 돌려 지출 총액 계산
                for (Integer expenseMoney : expenses) {
                    expenseSum += expenseMoney;
                }

                // AccountHistory 객체 생성
                AccountHistory accountHistory = AccountHistory.builder()
                                                    .mid(mid)
                                                    .monthIncome(incomeSum)
                                                    .monthExpense(expenseSum)
                                                    .monthSum(incomeSum - expenseSum)
                                                    .year(year)
                                                    .month(lastMonth)
                                                    .build();

                // AccountHistory List 객체에 값 저장
                accountHistories.add(accountHistory);
            }

            // DB 트랜잭션을 줄이기 위해 반복문 밖에서 일괄 저장
            accountHistoryRepository.saveAll(accountHistories);
        }

        log.info("AccountHistoryScheduler End Time = {}", LocalDateTime.now());
        log.info("========== AccountHistoryScheduler 동작 END ==========");
    }


    // 현재 연도 계산
    private static String getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        return formatter.format(new Date());
    }


    // 지난 연도 계산
    private static String getLastYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String date = formatter.format(new Date());
        int intDate = Integer.parseInt(date);

        return String.valueOf(intDate - 1);
    }


    // 현재 달 계산
    private static String getMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());

        return date.substring(4, 6);
    }


    // 지난 달 계산
    private static String getLastMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int intDate = Integer.parseInt(date);
        String stringDate = String.valueOf(intDate - 1);

        return stringDate.substring(4, 6);
    }
}
