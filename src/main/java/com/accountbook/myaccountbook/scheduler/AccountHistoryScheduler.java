package com.accountbook.myaccountbook.scheduler;

import com.accountbook.myaccountbook.persistence.AccountHistory;
import com.accountbook.myaccountbook.repository.AccountHistoryRepository;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import com.accountbook.myaccountbook.utils.AccountBookUtil;
import com.accountbook.myaccountbook.utils.SchedulerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * 지난 달의 수입/지출 금액을 계산해
     * AccountHistory 엔티티에 값을 저장
     */
    @Scheduled(cron = "0 0 0 1 * *")
    public void insertAccountHistory() {
        String year = AccountBookUtil.getYear(); // 현재 연도
        String lastYear = AccountBookUtil.getLastYear(); // 지난 연도
        String month = AccountBookUtil.getMonth(); // 현재 달
        String lastMonth = month.equals("1") ? "12": AccountBookUtil.getLastMonth(); // 지난 달
        String fullMonth = (month.equals("1") ? lastYear : year) + lastMonth; // 지난 달 yyyyMM

        log.info("========== {}년 {}월 1일 0시 0분 0초 진입 ==========", year, month);
        log.info("========== AccountHistoryScheduler 동작 START ==========");
        LocalDateTime startTime = LocalDateTime.now();

        // 빈 AccountHistory List 객체 생성
        List<AccountHistory> accountHistories = new ArrayList<>();

        // 현재 전체 사용자의 mid값을 리스트로 받음
        List<Integer> mids = memberRepository.findAllMid();
        log.info("조회된 사용자 수 = {}", mids.size());

        // AccountHistory에 사용자별 월별 기록 저장
        SchedulerUtil.insertAccountHistory(mids, incomeRepository, expenseRepository,
                            accountHistoryRepository, accountHistories, year, lastMonth, fullMonth);


        LocalDateTime endTime = LocalDateTime.now();
        log.info("========== AccountHistoryScheduler 동작 END ==========");

        Duration timeDiff = Duration.between(startTime, endTime);
        log.info("========== AccountHistoryScheduler 소요 시간 = {} ==========", timeDiff);
    }
}
