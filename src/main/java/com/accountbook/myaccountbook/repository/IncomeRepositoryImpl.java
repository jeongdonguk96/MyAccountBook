package com.accountbook.myaccountbook.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.accountbook.myaccountbook.entity.QIncome.income;

public class IncomeRepositoryImpl implements IncomeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public IncomeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Integer> findAllIncomeMoney(int mid, String month) {
        return queryFactory
                .select(income.incomeMoney)
                .from(income)
                .where(income.member.mid.eq(mid)
                        .and(income.month.eq(month)))
                .fetch();
    }
}
