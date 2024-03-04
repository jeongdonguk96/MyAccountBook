package com.accountbook.myaccountbook.batch.job;

import com.accountbook.myaccountbook.batch.listener.AccountHistoryJobListener;
import com.accountbook.myaccountbook.batch.processor.AccountHistoryItemProcessor;
import com.accountbook.myaccountbook.entity.AccountHistory;
import com.accountbook.myaccountbook.entity.Member;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
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
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    @Bean
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
                .<Member, AccountHistory>chunk(100)
                .reader(accountHistoryItemReader())
                .processor(accountHistoryItemProcessor())
                .writer(accountHistoryItemWriter())
                .build();
    }

    @Bean
    public JobExecutionListener accountHistoryJobListener() {
        return new AccountHistoryJobListener();
    }

    @Bean
    public ItemReader<Member> accountHistoryItemReader() {
        String sql = "SELECT mid FROM member";

        return new JdbcCursorItemReaderBuilder<Member>()
                .name("accountHistoryItemReader")
                .dataSource(dataSource)
                .sql(sql)
                .fetchSize(100)
                .beanRowMapper(Member.class)
                .build();
    }

    @Bean
    public ItemProcessor<Member, AccountHistory> accountHistoryItemProcessor() {
        return new AccountHistoryItemProcessor(incomeRepository, expenseRepository);
    }

    @Bean
    public ItemWriter<AccountHistory> accountHistoryItemWriter() {
        return new JpaItemWriterBuilder<AccountHistory>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

}
