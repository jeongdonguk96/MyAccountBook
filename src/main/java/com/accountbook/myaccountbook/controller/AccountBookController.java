package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.domain.AccountHistory;
import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseCategoryDto;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseReturnDto;
import com.accountbook.myaccountbook.dto.accountbook.IncomeReturnDto;
import com.accountbook.myaccountbook.dto.accounthistory.AccountHistoryDto;
import com.accountbook.myaccountbook.service.AccountBookService;
import com.accountbook.myaccountbook.service.AccountHistoryService;
import com.accountbook.myaccountbook.util.AccountBookUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/book")
@SessionAttributes("user")
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final AccountHistoryService accountHistoryService;
    private final AccountBookUtil accountBookUtil;


    @ModelAttribute("user")
    public Member setMember(@ModelAttribute Member member) {
        return member;
    }


    // 가계부 조회
    @GetMapping("")
    public String getAccountBook(Model model) {
        Member findMember = (Member) model.getAttribute("user"); // 세션
        String findYear = accountBookUtil.getYear(); // 현재 연도 yyyy
        String findMonth = accountBookUtil.getMonth(); // 현재 달 MM
        String lengthOfMonth = accountBookUtil.getDays(); // 현재 달의 일수
        String fullMonth = findYear + findMonth; // 연월 yyyyMM
        String message = accountBookUtil.getRandomMessage(); // 메시지 문구
        int incomeSum = 0; // 총 수입
        int expenseSum = 0; // 총 지출
        int restSum = 0; // 총 합계

        // 1. Income 엔티티 조회
        // 2. Income 엔티티를 Dto로 변환
        // 3. 총 수입 계산
        List<Income> incomes = accountBookService.findAllMonthIncome(fullMonth, findMember.getMid());
        List<IncomeReturnDto> incomeReturnDtos = accountBookService.imcomesToDto(incomes);
        incomeSum = accountBookService.totalizeIncome(incomes, incomeSum);

        // 1. Expense 엔티티 조회
        // 2. Expense 엔티티를 Dto로 변환
        // 3. 총 지출 계산
        List<Expense> expenses = accountBookService.findAllMonthExpense(fullMonth, findMember.getMid());
        List<ExpenseReturnDto> expenseReturnDtos = accountBookService.expensesToDto(expenses);
        expenseSum = accountBookService.totalizeExpense(expenses, expenseSum);

        // 총 합계 계산
        restSum = incomeSum - expenseSum;

        Map<String, Object> attribute = new HashMap<>();
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


    // 카테고리별 지출 파이차트 모달 조회
    @ResponseBody
    @PostMapping("/expenseCategory/{mid}")
    public List<Map<String, Object>> getPiechart(@PathVariable int mid) {
        String findYear = accountBookUtil.getYear(); // yyyy
        String findMonth = accountBookUtil.getMonth(); // MM
        String month = findYear+findMonth; // yyyyMM

        // Expense 엔티티를 조회하지만, Dto로 필요한 컬럼만 가져옴
        List<ExpenseCategoryDto> expenses = accountBookService.findAllExpenseCategoryByMonthAndMemberMid(month, mid);

        // 카테고리별 지출 정리
        Map<String, Integer> expenseMap = accountBookService.categorize(expenses);

        // 위에서 정리한 지출 Map을 List에 담음
        return accountBookService.expenseMapToMapList(expenseMap);
    }


    // 월별 지출 내역 모달 조회
    @ResponseBody
    @PostMapping("/expensesByMonth/{mid}")
    public ResponseDto<List<AccountHistoryDto>> getExpenseCategoryByMonth(@PathVariable int mid) {

        // DB에서 엔티티 조회
        List<AccountHistory> accountHistories = accountHistoryService.findAllAccountHistory(mid);

        // 엔티티를 Dto로 변환
        List<AccountHistoryDto> accountHistoryDtos = accountHistories.stream()
                .map(AccountHistoryDto::accountHistoryToDto)
                .toList();

        return new ResponseDto<>(HttpStatus.OK.value(), accountHistoryDtos, "success");
    }
}
