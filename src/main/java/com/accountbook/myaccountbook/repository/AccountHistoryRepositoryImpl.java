package com.accountbook.myaccountbook.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class AccountHistoryRepositoryImpl implements AccountHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AccountHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
