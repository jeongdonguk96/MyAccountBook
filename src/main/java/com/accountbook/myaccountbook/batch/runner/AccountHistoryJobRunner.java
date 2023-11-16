package com.accountbook.myaccountbook.batch.runner;

import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.newJob;

@Component
@RequiredArgsConstructor
public class AccountHistoryJobRunner implements ApplicationRunner {

    private final Scheduler scheduler;

    @Value("${accountHistory.scheduler.cron}")
    private String schedulerCron;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        JobDetail jobDetail = buildJobDetail(AccountHistorySchedulerJob.class, "accountHistoryJob", "batch", new HashMap<>());
        Trigger trigger = buildJobTrigger(schedulerCron);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                .build();
    }

    public JobDetail buildJobDetail(Class job, String name, String group, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return newJob(job).withIdentity(name, group)
                .usingJobData(jobDataMap)
                .build();
    }
}
