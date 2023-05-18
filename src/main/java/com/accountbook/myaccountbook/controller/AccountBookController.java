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

        String findYear = getYear(); // 현재 연도
        String findMonth = getMonth(); // 현재 달
        String lengthOfMonth = getDays(); // 현재 달의 일수

        model.addAttribute("year", findYear);
        model.addAttribute("month", findMonth);
        model.addAttribute("days", lengthOfMonth);
        model.addAttribute("income", new Income());
        model.addAttribute("expense", new Expense());

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
