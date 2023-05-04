package com.accountbook.myaccountbook.repository;

import com.accountbook.myaccountbook.domain.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountBookRepository extends JpaRepository<AccountBook, Integer> {

    // 가계부 날짜 중복 조회
    AccountBook findByDate(String date);

    // 가계부 연도별 조회
    AccountBook findByYear(String year);

    // 가계부 월별 조회
    AccountBook findByMonth(String month);

    // 사용자별 가계부 조회
    List<AccountBook> findAllByMemberMid(int mid);
}
