package com.accountbook.myaccountbook.batch.reader;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.SneakyThrows;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class QuerydslNoOffsetPagingItemReader<T> extends AbstractPagingItemReader<T> {

    protected Function<JPAQueryFactory, JPAQuery<T>> queryFunction;
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;

    @Override
    @SneakyThrows
    protected void doReadPage() {
        super.doOpen();
        entityManager = entityManagerFactory.createEntityManager();

        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
        JPAQuery<T> query = createQuery()
                .offset(getPage() * getPageSize())
                .limit(getPageSize());

        initResults();

    }

    protected JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFunction.apply(queryFactory);
    }

    protected void initResults() {
        if (CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }

    protected void fetchQuery(JPAQuery<T> query) {
        List<T> queryResult = query.fetch();
        for (T entity : queryResult) {
            entityManager.detach(entity);
            results.add(entity);
        }
    }
}
