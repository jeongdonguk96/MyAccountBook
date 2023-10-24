package com.accountbook.myaccountbook.utils;

import com.accountbook.myaccountbook.dto.accountbook.ExpenseCategoryDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ExpenseUtil {

    // 지출 기록을 모두 가져와 카테고리 맵으로 변환한다.
    public static Map<String, Integer> expensesToMap(List<ExpenseCategoryDto> expenses) {
        Map<String, Integer> expenseMap = new HashMap<>();

        for (ExpenseCategoryDto expense : expenses) {
            String category = expense.getExpenseCategory().toString();
            int money = expense.getExpenseMoney();

            // Map에서 같은 key를 가진 value들은 합산해서 적용한다.
            if (expenseMap.containsKey(category)) {
                int totalMoney = expenseMap.get(category);
                totalMoney += money;
                expenseMap.put(category, totalMoney);
            } else {
                expenseMap.put(category, money);
            }
        }

        return expenseMap;
    }


    // 지출 카테고리 맵을 리스트로 변환한다.
    public static List<Map<String, Object>> mapToList(Map<String, Integer> expenseMap) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (Map.Entry<String, Integer> expense : expenseMap.entrySet()) {
            String category = expense.getKey();
            int money = expense.getValue();

            Map<String, Object> map = new HashMap<>();
            map.put("category", category);
            map.put("money", money);

            mapList.add(map);
        }

        return mapList;
    }

}
