package com.accountbook.myaccountbook.batch.listener;

import com.accountbook.myaccountbook.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class AccountHistoryJobListener implements JobExecutionListener {

    String year = DateUtil.getYear(); // 현재 연도 (yyyy)
    String month = DateUtil.getMonth(); // 현재 달 (MM)
    long startTime = 0L;
    long endTime = 0L;
    long timeDiff = 0L;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("========== {}년 {}월 1일 0시 0분 0초 진입 ==========", year, month);
        log.info("========== AccountHistoryScheduler 동작 START ==========");
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        endTime = System.currentTimeMillis();
        log.info("========== AccountHistoryScheduler 동작 END ==========");

        timeDiff = (endTime - startTime);
        log.info("========== AccountHistoryScheduler 소요 시간 = {}ms ==========", timeDiff);
    }
}
