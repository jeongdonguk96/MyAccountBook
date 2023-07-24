package com.accountbook.myaccountbook.repository;

import java.util.List;

public interface ExpenseRepositoryCustom {
    List<Integer> findAllExpenseMoney(int mid, String month);
}
