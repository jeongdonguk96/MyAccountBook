package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.accountbook.*;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    /**
     * 수입 작성
     * @param incomeWriteDto 수입 작성 Dto
     */
    @Transactional
    public void writeIncome(IncomeWriteDto incomeWriteDto) {
        // 잔여금 계산
        Member findMember = memberRepository.findById(incomeWriteDto.getMid()).get();
        memberService.setRest(findMember, incomeWriteDto.getIncomeMoney());

        // 년, 월, 일 계산
        String month = incomeWriteDto.getMonth();
        String year = month.substring(0,4);

        Income income = Income.builder()
                            .incomeMoney(incomeWriteDto.getIncomeMoney())
                            .incomeReason(incomeWriteDto.getIncomeReason())
                            .year(year)
                            .month(month)
                            .member(findMember)
                            .build();

        incomeRepository.save(income);
    }


    /**
     * 지출 작성
     * @param expenseWriteDto 지출 작성 Dto
     */
    @Transactional
    public void writeExpense(ExpenseWriteDto expenseWriteDto) {
        // 잔여금 계산
        Member findMember = memberRepository.findById(expenseWriteDto.getMid()).get();
        memberService.setRest(findMember, -expenseWriteDto.getExpenseMoney());

        // 년, 월, 일 계산
        String date = expenseWriteDto.getDate();
        String year = date.substring(0, 4);
        String month = date.substring(0,6);

        Expense expense = Expense.builder()
                            .expenseMoney(expenseWriteDto.getExpenseMoney())
                            .expenseReason(expenseWriteDto.getExpenseReason())
                            .expenseCategory(expenseWriteDto.getExpenseCategory())
                            .year(year)
                            .month(month)
                            .date(date)
                            .member(findMember)
                            .build();

        expenseRepository.save(expense);
    }


    /**
     * 수입 수정
     * @param incomeModifyDto 수입 수정 Dto
     */
    @Transactional
    public void modifyIncome(IncomeModifyDto incomeModifyDto) {
        Member findMember = memberRepository.findById(incomeModifyDto.getMid()).get();
        Income findIncome = incomeRepository.findById(incomeModifyDto.getInid()).get();

        // 기존 incomeMoney 롤백
        memberService.rollbackRest(findMember, -findIncome.getIncomeMoney());

        findIncome.setIncomeReason(incomeModifyDto.getIncomeReason());
        findIncome.setIncomeMoney(incomeModifyDto.getIncomeMoney());

        memberService.setRest(findMember, findIncome.getIncomeMoney());
    }


    /**
     * 지출 수정
     * @param expenseModifyDto 지출 수정 Dto
     */
    @Transactional
    public void modifyExpense(ExpenseModifyDto expenseModifyDto) {
        Member findMember = memberRepository.findById(expenseModifyDto.getMid()).get();
        Expense findExpense = expenseRepository.findById(expenseModifyDto.getExid()).get();

        // 기존 expenseMoney 롤백
        memberService.rollbackRest(findMember, findExpense.getExpenseMoney());

        findExpense.setExpenseMoney(expenseModifyDto.getExpenseMoney());
        findExpense.setExpenseReason(expenseModifyDto.getExpenseReason());
        findExpense.setExpenseCategory(expenseModifyDto.getExpenseCategory());

        memberService.setRest(findMember, -findExpense.getExpenseMoney());
    }


    /**
     * 수입 삭제
     * @param incomeDeleteDto 수입 삭제 Dto
     */
    @Transactional
    public void deleteIncome(@RequestBody IncomeDeleteDto incomeDeleteDto) {
        Member findMember = memberRepository.findById(incomeDeleteDto.getMid()).get();
        Income findIncome = incomeRepository.findById(incomeDeleteDto.getInid()).get();

        // 삭제하는 수입금만큼 member의 rest 차감
        memberService.setRest(findMember, -findIncome.getIncomeMoney());

        incomeRepository.deleteById(incomeDeleteDto.getInid());
    }


    /**
     * 지출 삭제
     * @param expenseDeleteDto 지출 삭제 Dto
     */
    @Transactional
    public void deleteExpense(ExpenseDeleteDto expenseDeleteDto) {
        Member findMember = memberRepository.findById(expenseDeleteDto.getMid()).get();
        Expense findExpense = expenseRepository.findById(expenseDeleteDto.getExid()).get();

        // 삭제하는 지출금만큼 member의 rest 증가
        memberService.setRest(findMember, findExpense.getExpenseMoney());

        expenseRepository.deleteById(expenseDeleteDto.getExid());
    }


    /**
     * 카테고리별 지출 목록 조회
     * @param month 이번 달
     * @param mid 사용자 id
     * @return 사용자별 지출 목록 List
     */
    @Transactional
    public List<ExpenseCategoryDto> findAllExpenseCategoryByMonthAndMemberMid(String month, int mid) {
        return expenseRepository.findAllExpenseCategoryByMonthAndMemberMid(month, mid);
    }


    /**
     * 이 달의 수입 전체 조회
     * @param mid 사용자 id
     * @return 사용자별 수입 전체 List
     */
    @Transactional
    public List<Income> findAllMonthIncome(String month, int mid) {
        return incomeRepository.findAllByMonthAndMemberMid(month, mid);
    }


    /**
     * 이 달의 지출 전체 조회
     * @param mid 사용자 id
     * @return 사용자별 지출 전체 List
     */
    @Transactional
    public List<Expense> findAllMonthExpense(String month, int mid) {
        return expenseRepository.findAllByMonthAndMemberMid(month, mid);
    }


    /**
     * 카테고리별 지출 정리
     * @param expenses 월별 소비 List (카테고리, 금액)
     * @return 카테고리별 지출 Map
     */
    public Map<String, Integer> categorize(List<ExpenseCategoryDto> expenses) {
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

        return expenseMap;
    }


    /**
     * 카테고리별 지출 Map을 List로 변환
     * @param expenseMap 카테고리별 지출 Map
     * @return 카테고리별 지출 MapList
     */
    public List<Map<String, Object>> expenseMapToMapList(Map<String, Integer> expenseMap) {
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


    /**
     * Income 엔티티를 Dto로 변환
     * @param incomes Income 엔티티
     * @return 변환한 Income Dto
     */
    public List<IncomeReturnDto> imcomesToDto(List<Income> incomes) {
        List<IncomeReturnDto> incomeReturnDtos = new ArrayList<>();


        for (Income income : incomes) {
            IncomeReturnDto incomeReturnDto = new IncomeReturnDto();
            incomeReturnDto.convetToDto(income);
            incomeReturnDtos.add(incomeReturnDto);
        }

        return incomeReturnDtos;
    }

    
    /**
     * Expense 엔티티를 Dto로 변환
     * @param expenses Expense 엔티티
     * @return 변환한 Expense Dto
     */
    public List<ExpenseReturnDto> expensesToDto(List<Expense> expenses) {
        List<ExpenseReturnDto> expenseReturnDtos = new ArrayList<>();
        
        for (Expense expense : expenses) {
            ExpenseReturnDto expenseReturnDto = new ExpenseReturnDto();
            expenseReturnDto.convetToDto(expense);
            expenseReturnDtos.add(expenseReturnDto);
        }

        return expenseReturnDtos;
    }


    /**
     * 수입 총액 계산
     * @param incomes Expense 엔티티
     * @param incomeSum 빈 수입 총액 변수
     * @return 수입 총액
     */
    public int totalizeIncome(List<Income> incomes, int incomeSum) {
        for (Income income : incomes) {
            incomeSum += income.getIncomeMoney();
        }

        return incomeSum;
    }


    /**
     * 지출 총액 계산
     * @param expenses Expense 엔티티
     * @param expenseSum 빈 지출 총액 변수
     * @return 지출 총액
     */
    public int totalizeExpense(List<Expense> expenses, int expenseSum) {
        for (Expense expense : expenses) {
            expenseSum += expense.getExpenseMoney();
        }

        return expenseSum;
    }
}
