package com.accountbook.myaccountbook.controller;

import com.accountbook.myaccountbook.constant.MessageConstants;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/book")
@SessionAttributes("user")
@RequiredArgsConstructor
public class AccountBookController {

    private final AccountBookService accountBookService;
    private final AccountHistoryService accountHistoryService;


    @ModelAttribute("user")
    public Member setMember(@ModelAttribute Member member) {
        return member;
    }


    // 가계부 조회
    @GetMapping("/")
    public String getAccountBook(Model model) {
        Member findMember = (Member) model.getAttribute("user"); // 세션
        String findYear = getYear(); // 현재 연도 yyyy
        String findMonth = getMonth(); // 현재 달 MM
        String lengthOfMonth = getDays(); // 현재 달의 일수
        String fullMonth = findYear + findMonth; // 연월 yyyyMM
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


    // 카테고리별 지출 파이차트 모달 조회
    @ResponseBody
    @PostMapping("/expenseCategory/{mid}")
    public List<Map<String, Object>> getPiechart(@PathVariable int mid) {
        String findYear = getYear(); // yyyy
        String findMonth = getMonth(); // MM
        String month = findYear+findMonth; // yyyyMM

        // Expense 엔티티를 조회하지만, Dto로 필요한 컬럼만 가져옴
        List<ExpenseCategoryDto> expenses = accountBookService.findAllExpenseCategoryByMonthAndMemberMid(month, mid);

        // 중복된 카테고리명으로 된 지출을 동일한 카테고리로 합산해줌
        Map<String, Integer> expenseMap = new HashMap<>();
        for (ExpenseCategoryDto expense : expenses) {
            String category = expense.getExpenseCategory().toString();
            int money = expense.getExpenseMoney();

            // Map에서 같은 key를 가진 value들은 합산해서 적용
            if (expenseMap.containsKey(category)) {
                int totalMoney = expenseMap.get(category);
                totalMoney += money;
                expenseMap.put(category, totalMoney);
            } else {
                expenseMap.put(category, money);
            }
        }

        // 지출을 담은 Map을 List에 담음
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


    // 월별 지출 내역 모달 조회
    @ResponseBody
    @PostMapping("/book/expensesByMonth/{mid}")
    public ResponseDto<List<AccountHistoryDto>> getExpenseCategoryByMonth(@PathVariable int mid) {

        // DB에서 엔티티 조회
        List<AccountHistory> accountHistories = accountHistoryService.findAllAccountHistory(mid);

        // 엔티티를 Dto로 변환
        List<AccountHistoryDto> accountHistoryDtos = accountHistories.stream()
                .map(AccountHistoryDto::accountHistoryToDto)
                .toList();

        return new ResponseDto<>(HttpStatus.OK.value(), accountHistoryDtos, "success");
    }


    // 현재 연도 계산
    private static String getYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

        return formatter.format(new Date());
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
