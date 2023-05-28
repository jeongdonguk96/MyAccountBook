package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.constant.MessageConstants;
import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseCategoryDto;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseReturnDto;
import com.accountbook.myaccountbook.dto.accountbook.IncomeReturnDto;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Controller
@SessionAttributes("user")
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final ExpenseRepository expenseRepository;


    @ModelAttribute("user")
    public Member setMember(@ModelAttribute Member member) {
        return member;
    }


    // 가계부 조회
    @GetMapping("/book")
    public String getAccountBook(Model model) {
        Member findMember = (Member) model.getAttribute("user"); // 세션
        String findYear = getYear(); // 현재 연도
        String findMonth = getMonth(); // 현재 달
        String lengthOfMonth = getDays(); // 현재 달의 일수
        String fullMonth = findYear + findMonth; // 연월
        String message = getRandomMessage(); // 메시지 문구
        int incomeSum = 0; // 총 수입
        int expenseSum = 0; // 총 지출
        int restSum = 0; // 총 합계

        // 총 수입 계산, Income 엔티티를 Dto로
        List<Income> incomes = accountBookService.findAllMonthIncome(fullMonth, findMember.getMid());
        List<IncomeReturnDto> incomeReturnDtos = new ArrayList<>();

        for (Income income : incomes) {
            incomeSum += income.getIncomeMoney();
        }

        for (Income income : incomes) {
            IncomeReturnDto incomeReturnDto = new IncomeReturnDto();
            incomeReturnDto.convetToDto(income);
            incomeReturnDtos.add(incomeReturnDto);
        }

        // 총 지출 계산, Expense 엔티티를 Dto로
        List<Expense> expenses = accountBookService.findAllMonthExpense(fullMonth, findMember.getMid());
        List<ExpenseReturnDto> expenseReturnDtos = new ArrayList<>();

        for (Expense expense : expenses) {
            expenseSum += expense.getExpenseMoney();
        }

        for (Expense expense : expenses) {
            ExpenseReturnDto expenseReturnDto = new ExpenseReturnDto();
            expenseReturnDto.convetToDto(expense);
            expenseReturnDtos.add(expenseReturnDto);
        }

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


    // 카테고리별 지출 파이차트 조회
    @ResponseBody
    @PostMapping ("/book/expenseCategory/{mid}")
    public List<Map<String, Object>> getPiechart(@PathVariable int mid) {

        // Expense 엔티티를 조회하지만, Dto로 필요한 컬럼만 가져옴
        List<ExpenseCategoryDto> expenses = expenseRepository.findAllExpenseCategoryByMemberMid(mid);

        List<Map<String, Object>> mapList = new ArrayList<>();

        for (ExpenseCategoryDto expense : expenses) {
            Map<String, Object> map = new HashMap<>();

            switch (expense.getExpenseCategory()) {
                case "식료품" -> {
                    map.put("category", "식료품");
                    map.put("money", expense.getExpenseMoney());
                }
                
                case "외식" -> {
                    map.put("category", "외식");
                    map.put("money", expense.getExpenseMoney());
                }

                case "배달" -> {
                    map.put("category", "배달");
                    map.put("money", expense.getExpenseMoney());
                }

                case "생필품" -> {
                    map.put("category", "생필품");
                    map.put("money", expense.getExpenseMoney());
                }

                case "가전제품" -> {
                    map.put("category", "가전제품");
                    map.put("money", expense.getExpenseMoney());
                }

                case "전자제품" -> {
                    map.put("category", "전자제품");
                    map.put("money", expense.getExpenseMoney());
                }

                case "의류" -> {
                    map.put("category", "의류");
                    map.put("money", expense.getExpenseMoney());
                }

                case "의료비" -> {
                    map.put("category", "의료비");
                    map.put("money", expense.getExpenseMoney());
                }

                case "통신비" -> {
                    map.put("category", "통신비");
                    map.put("money", expense.getExpenseMoney());
                }

                case "여가활동" -> {
                    map.put("category", "여가활동");
                    map.put("money", expense.getExpenseMoney());
                }

                case "예적금" -> {
                    map.put("category", "예적금");
                    map.put("money", expense.getExpenseMoney());
                }

                case "보험료" -> {
                    map.put("category", "보험료");
                    map.put("money", expense.getExpenseMoney());
                }

                case "집세" -> {
                    map.put("category", "집세");
                    map.put("money", expense.getExpenseMoney());
                }

                case "공과금" -> {
                    map.put("category", "공과금");
                    map.put("money", expense.getExpenseMoney());
                }

                case "대출상환금" -> {
                    map.put("category", "대출상환금");
                    map.put("money", expense.getExpenseMoney());
                }

                case "경조사비" -> {
                    map.put("category", "경조사비");
                    map.put("money", expense.getExpenseMoney());
                }

                case "기타" -> {
                    map.put("category", "기타");
                    map.put("money", expense.getExpenseMoney());
                }
            }
            mapList.add(map);
        }

        return mapList;
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


    // 랜덤 문구 추첨
    private static String getRandomMessage() {
        int index = new Random().nextInt(MessageConstants.MESSAGE.length);

        return MessageConstants.MESSAGE[index];
    }

}
