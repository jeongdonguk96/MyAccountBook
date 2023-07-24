package com.accountbook.myaccountbook.repository;

import java.util.List;

public interface IncomeRepositoryCustom {
    List<Integer> findAllIncomeMoney(int mid, String month);
}
