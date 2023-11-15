package com.accountbook.myaccountbook.batch.job;

import com.accountbook.myaccountbook.persistence.AccountHistory;
import com.accountbook.myaccountbook.persistence.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class AccountHistoryJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;


    public Job accountHistoryJob() {
        return jobBuilderFactory.get("accountHistoryJob")
                .incrementer(new RunIdIncrementer())
                .start(accountHistoryStep())
                .listener(accountHistoryJobListener())
                .build();
    }

    @Bean
    public Step accountHistoryStep() {
        return stepBuilderFactory.get("accountHistoryStep")
                .<Member, AccountHistory>chunk(10)
                .reader(accountHistoryItemReader())
                .processor(accountHistoryItemProcessor())
                .writer(accountHistoryItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? extends Member> accountHistoryItemReader() {
        return new JDB
    }

}
