package com.accountbook.myaccountbook.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.accountbook.myaccountbook.persistence.QExpense.expense;

public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ExpenseRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Integer> findAllExpenseMoney(int mid, String month) {
        return queryFactory
                .select(expense.expenseMoney)
                .from(expense)
                .where(expense.member.mid.eq(mid)
                        .and(expense.month.eq(month)))
                .fetch();
    }
}
