package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.dto.accountbook.*;
import com.accountbook.myaccountbook.enums.ExpenseCategoryEnum;
import com.accountbook.myaccountbook.exception.CustomApiException;
import com.accountbook.myaccountbook.entity.Expense;
import com.accountbook.myaccountbook.entity.Income;
import com.accountbook.myaccountbook.entity.Member;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import com.accountbook.myaccountbook.utils.ExpenseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;


    /**
     * 수입 작성
     * @param incomeWriteDto 수입 작성 Dto
     */
    @Transactional
    public void writeIncome(IncomeWriteDto incomeWriteDto) {
        Member findMember = memberRepository.findById(incomeWriteDto.getMid()).orElseThrow(
                () -> new CustomApiException("사용자가 없습니다.")
        );

        // 잔여금을 계산해 수정한다.
        findMember.increaseRest(incomeWriteDto.getIncomeMoney());

        // 년, 월을 계산한다.
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
        Member findMember = memberRepository.findById(expenseWriteDto.getMid()).orElseThrow(
                () -> new CustomApiException("사용자가 없습니다.")
        );

        // 잔여금을 계산해 수정한다.
        findMember.decreaseRest(expenseWriteDto.getExpenseMoney());

        // 년, 월, 일을 계산한다.
        String date = expenseWriteDto.getDate();
        String year = date.substring(0, 4);
        String month = date.substring(0,6);

        Expense expense = Expense.builder()
                            .expenseMoney(expenseWriteDto.getExpenseMoney())
                            .expenseReason(expenseWriteDto.getExpenseReason())
                            .expenseCategory(ExpenseCategoryEnum.valueOf(expenseWriteDto.getExpenseCategory()))
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
        Member findMember = memberRepository.findById(incomeModifyDto.getMid()).orElseThrow(
                () -> new CustomApiException("사용자가 없습니다.")
        );
        Income findIncome = incomeRepository.findById(incomeModifyDto.getInid()).orElseThrow(
                () -> new CustomApiException("수입 내역이 없습니다.")
        );

        // 해당 수입을 기존 잔여금에서 롤백한다.
        findMember.decreaseRest(findIncome.getIncomeMoney());

        // 입력받은 데이터로 수입 내용을 수정한다.
        findIncome.modifyReasonAndMoney(incomeModifyDto.getIncomeReason(), incomeModifyDto.getIncomeMoney());

        // 최종 잔여금을 계산한다.
        findMember.increaseRest(incomeModifyDto.getIncomeMoney());
    }


    /**
     * 지출 수정
     * @param expenseModifyDto 지출 수정 Dto
     */
    @Transactional
    public void modifyExpense(ExpenseModifyDto expenseModifyDto) {
        Member findMember = memberRepository.findById(expenseModifyDto.getMid()).orElseThrow(
                () -> new CustomApiException("사용자가 없습니다.")
        );
        Expense findExpense = expenseRepository.findById(expenseModifyDto.getExid()).orElseThrow(
                () -> new CustomApiException("지출 내역이 없습니다.")
        );

        // 해당 지출을 기존 잔여금에서 롤백한다.
        findMember.increaseRest(findExpense.getExpenseMoney());

        // 입력받은 데이터로 지출 내용을 수정한다.
        findExpense.modifyReasonAndMoneyAndCategory(expenseModifyDto.getExpenseReason(), expenseModifyDto.getExpenseMoney(), ExpenseCategoryEnum.valueOf(expenseModifyDto.getExpenseCategory()));

        // 최종 잔여금을 계산한다.
        findMember.decreaseRest(expenseModifyDto.getExpenseMoney());
    }


    /**
     * 수입 삭제
     * @param incomeDeleteDto 수입 삭제 Dto
     */
    @Transactional
    public void deleteIncome(@RequestBody IncomeDeleteDto incomeDeleteDto) {
        Member findMember = memberRepository.findById(incomeDeleteDto.getMid()).orElseThrow(
                () -> new CustomApiException("사용자가 없습니다.")
        );
        Income findIncome = incomeRepository.findById(incomeDeleteDto.getInid()).orElseThrow(
                () -> new CustomApiException("수입 내역이 없습니다.")
        );

        // 삭제하는 수입금만큼 member의 rest를 차감한다.
        findMember.decreaseRest(findIncome.getIncomeMoney());

        // 수입을 삭제한다.
        incomeRepository.deleteById(incomeDeleteDto.getInid());
    }


    /**
     * 지출 삭제
     * @param expenseDeleteDto 지출 삭제 Dto
     */
    @Transactional
    public void deleteExpense(ExpenseDeleteDto expenseDeleteDto) {
        Member findMember = memberRepository.findById(expenseDeleteDto.getMid()).orElseThrow(
                () -> new CustomApiException("사용자가 없습니다.")
        );
        Expense findExpense = expenseRepository.findById(expenseDeleteDto.getExid()).orElseThrow(
                () -> new CustomApiException("지출 내역이 없습니다.")
        );

        // 삭제하는 지출금만큼 member의 rest를 증가한다.
        findMember.increaseRest(findExpense.getExpenseMoney());

        // 지출을 삭제한다.
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
     * 이 달의 수입 전체 조회 후 Dto로 변환
     * @param mid 사용자 id
     * @return 사용자별 수입 전체 List
     */
    @Transactional
    public List<IncomeReturnDto> findAllMonthIncomeToDto(String month, int mid) {
        List<Income> findIncomes = incomeRepository.findAllByMonthAndMemberMid(month, mid);

        return findIncomes.stream()
                .map(IncomeReturnDto::convertToDto)
                .collect(Collectors.toList());
    }


    /**
     * 이 달의 지출 전체 조회 후 Dto로 변환
     * @param mid 사용자 id
     * @return 사용자별 지출 전체 List
     */
    @Transactional
    public List<ExpenseReturnDto> findAllMonthExpenseToDto(String month, int mid) {
        List<Expense> findExpense = expenseRepository.findAllByMonthAndMemberMid(month, mid);

        return findExpense.stream()
                .map(ExpenseReturnDto::convertToDto)
                .collect(Collectors.toList());
    }


    /**
     * 카테고리별 지출 정리
     * @param expenses 월별 소비 List (카테고리, 금액)
     * @return 카테고리별 지출 Map
     */
    public Map<String, Integer> categorize(List<ExpenseCategoryDto> expenses) {
        return ExpenseUtil.expensesToMap(expenses);
    }


    /**
     * 카테고리별 지출 Map을 List로 변환
     * @param expenseMap 카테고리별 지출 Map
     * @return 카테고리별 지출 MapList
     */
    public List<Map<String, Object>> expenseMapToMapList(Map<String, Integer> expenseMap) {
        return ExpenseUtil.mapToList(expenseMap);
    }


    /**
     * 수입 총액 계산
     * @param incomeReturnDtos Expense Dto
     * @param incomeSum 빈 수입 총액 변수
     * @return 수입 총액
     */
    public int totalizeIncome(List<IncomeReturnDto> incomeReturnDtos, int incomeSum) {
        for (IncomeReturnDto income : incomeReturnDtos) {
            incomeSum += income.getIncomeMoney();
        }

        return incomeSum;
    }


    /**
     * 지출 총액 계산
     * @param expenseReturnDtos Expense Dto
     * @param expenseSum 빈 지출 총액 변수
     * @return 지출 총액
     */
    public int totalizeExpense(List<ExpenseReturnDto> expenseReturnDtos, int expenseSum) {
        for (ExpenseReturnDto expense : expenseReturnDtos) {
            expenseSum += expense.getExpenseMoney();
        }

        return expenseSum;
    }
}
