package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AccountBookService accountBookService;

    // 초기 화면
    @GetMapping("/")
    public String firstView() {
        return "main";
    }

    // 회원가입 화면
    @GetMapping("/join")
    public String getJoinView() {
        return "member/join";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String getLoginView() {
        return "member/login";
    }

    // 메인 화면
    @PostMapping("/accountBook")
    public String getMainView(int mid, Model model) {
        List<Income> incomes = accountBookService.findAllIncome(mid);
        List<Expense> expenses = accountBookService.findAllExpense(mid);

        model.addAttribute("incomes", incomes);
        model.addAttribute("expenses", expenses);

        return "accountbook/book";
    }

}
