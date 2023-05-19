package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@SessionAttributes("user")
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;


    @ModelAttribute("user")
    public Member setMember(@ModelAttribute Member member) {
        return member;
    }


    // 가계부 조회
    @GetMapping("/book")
    public String getAccountBook(Model model) {
        Member findMember = (Member) model.getAttribute("user");
        String findYear = getYear(); // 현재 연도
        String findMonth = getMonth(); // 현재 달
        String lengthOfMonth = getDays(); // 현재 달의 일수
        String fullMonth = findYear + findMonth; // 연월

        int incomeSum = 0;
        int expenseSum = 0;
        int restSum = 0;

        // 총 수입 계산
        List<Income> incomes = accountBookService.findAllMonthIncome(fullMonth, findMember.getMid());

        for (Income income : incomes) {
            incomeSum += income.getIncomeMoney();
        }

        // 총 지출 계산
        List<Expense> expenses = accountBookService.findAllMonthExpense(fullMonth, findMember.getMid());

        for (Expense expense : expenses) {
            expenseSum += expense.getExpenseMoney();
        }

        // 총 합계 계산
        restSum = incomeSum - expenseSum;

        model.addAttribute("year", findYear);
        model.addAttribute("month", findMonth);
        model.addAttribute("days", lengthOfMonth);
        model.addAttribute("income", new Income());
        model.addAttribute("expense", new Expense());
        model.addAttribute("incomeSum", incomeSum);
        model.addAttribute("expenseSum", expenseSum);
        model.addAttribute("restSum", restSum);

        return "accountbook/book";
    }


    // 현재 연도 계산
    private static String getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());

        return date.substring(0, 4);
    }


    // 현재 달 계산
    private static String getMonth() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());

        return date.substring(4, 6);
    }


    // 현재 달의 일수 계산
    private static String getDays() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String date = formatter.format(new Date());
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));

        LocalDate findMonth = LocalDate.of(year, month, 1);
        return String.valueOf(findMonth.lengthOfMonth());
    }


}
