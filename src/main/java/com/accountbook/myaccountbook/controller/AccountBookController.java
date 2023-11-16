package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseCategoryDto;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseReturnDto;
import com.accountbook.myaccountbook.dto.accountbook.IncomeReturnDto;
import com.accountbook.myaccountbook.dto.accounthistory.AccountHistoryDto;
import com.accountbook.myaccountbook.persistence.Expense;
import com.accountbook.myaccountbook.persistence.Income;
import com.accountbook.myaccountbook.persistence.Member;
import com.accountbook.myaccountbook.service.AccountBookService;
import com.accountbook.myaccountbook.service.AccountHistoryService;
import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import com.accountbook.myaccountbook.utils.AccountBookUtil;
import com.accountbook.myaccountbook.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final AccountHistoryService accountHistoryService;


    // 가계부 화면을 조회한다.
    @GetMapping("")
    public String getAccountBook(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = userDetails.getMember();
        String findYear = DateUtil.getYear(); // 현재 연도 yyyy
        String findMonth = DateUtil.getMonth(); // 현재 달 MM
        String lengthOfMonth = DateUtil.getDays(); // 현재 달의 일수
        String fullMonth = findYear + findMonth; // 연월 yyyyMM
        String message = AccountBookUtil.getRandomMessage(); // 메시지 문구
        int incomeSum = 0; // 총 수입
        int expenseSum = 0; // 총 지출
        int restSum = 0; // 총 합계

        // 1. Income 엔티티를 Dto로 조회한다.
        // 2. 총 수입을 계산한다.
        List<IncomeReturnDto> incomeReturnDtos = accountBookService.findAllMonthIncomeToDto(fullMonth, member.getMid());
        incomeSum = accountBookService.totalizeIncome(incomeReturnDtos, incomeSum);

        // 1. Expense 엔티티를 Dto로 조회한다.
        // 2. 총 지출을 계산한다.
        List<ExpenseReturnDto> expenseReturnDtos = accountBookService.findAllMonthExpenseToDto(fullMonth, member.getMid());
        expenseSum = accountBookService.totalizeExpense(expenseReturnDtos, expenseSum);

        // 총 합계를 계산한다.
        restSum = incomeSum - expenseSum;

        // 모델에 값을 담아준다.
        Map<String, Object> attribute = new HashMap<>();
        attribute.put("user", member);
        attribute.put("year", findYear);
        attribute.put("month", findMonth);
        attribute.put("days", lengthOfMonth);
        attribute.put("income", new Income());
        attribute.put("expense", new Expense());
        attribute.put("incomeSum", incomeSum);
        attribute.put("expenseSum", expenseSum);
        attribute.put("restSum", restSum);
        attribute.put("incomes", incomeReturnDtos);
        attribute.put("expenses", expenseReturnDtos);
        attribute.put("message", message);

        model.addAllAttributes(attribute);

        return "accountbook/book";
    }


    // 카테고리별 지출 파이차트 모달을 조회한다.
    @ResponseBody
    @PostMapping("/expenseCategory/{mid}")
    public List<Map<String, Object>> getPiechart(@PathVariable int mid) {
        String findYear = DateUtil.getYear(); // yyyy
        String findMonth = DateUtil.getMonth(); // MM
        String month = findYear+findMonth; // yyyyMM

        // Expense 엔티티를 조회하지만, Dto로 필요한 컬럼만 가져온다.
        List<ExpenseCategoryDto> expenses = accountBookService.findAllExpenseCategoryByMonthAndMemberMid(month, mid);

        // 카테고리별 지출을 정리한다.
        Map<String, Integer> expenseMap = accountBookService.categorize(expenses);

        // 위에서 정리한 지출 Map을 List에 담는다.
        return accountBookService.expenseMapToMapList(expenseMap);
    }


    // 월별 지출 내역 모달을 조회한다.
    @ResponseBody
    @PostMapping("/expensesByMonth/{mid}")
    public ResponseDto<List<AccountHistoryDto>> getExpenseCategoryByMonth(@PathVariable int mid) {
        // AccountHistory 엔티티를 Dto로 조회한다.
        System.out.println("mid = " + mid);
        List<AccountHistoryDto> accountHistoryDtos = accountHistoryService.findAllAccountHistoryToDto(mid);
        System.out.println("accountHistoryDtos = " + accountHistoryDtos);

        return new ResponseDto<>(HttpStatus.OK.value(), accountHistoryDtos, "success");
    }
}
