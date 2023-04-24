package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AccountBookService {
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 수입 작성
     * @param mid 사용자 id
     * @param income 수입
     */
    @Transactional
    public void writeIncome(int mid, Income income) {
        Member findMember = memberRepository.findById(mid).get();

        income.setMember(findMember);

        memberService.setRest(findMember, income.getMoney());

        incomeRepository.save(income);
    }

    /**
     * 지출 작성
     * @param mid 사용자 id
     * @param expense 지출
     */
    @Transactional
    public void writeExpense(int mid, Expense expense) {
        Member findMember = memberRepository.findById(mid).get();

        expense.setMember(findMember);

        memberService.setRest(findMember, expense.getMoney());

        expenseRepository.save(expense);
    }

    /**
     * 수입 수정
     * @param mid 사용자 id
     * @param income 수정할 수입
     */
    @Transactional
    public void modifyIncome(int mid, Income income) {
        Member findMember = memberRepository.findById(mid).get();
        Income findIncome = incomeRepository.findById(income.getInid()).get();

        findIncome.setReason(income.getReason());
        findIncome.setMoney(income.getMoney());

        memberService.setRest(findMember, findIncome.getMoney());
    }

    /**
     * 지출 수정
     * @param mid 사용자 id
     * @param expense 수정할 지출
     */
    @Transactional
    public void modifyExpense(int mid, Expense expense) {
        Member findMember = memberRepository.findById(mid).get();
        Expense findExpense = expenseRepository.findById(expense.getExid()).get();

        findExpense.setReason(expense.getReason());
        findExpense.setMoney(expense.getMoney());

        memberService.setRest(findMember, findExpense.getMoney());
    }

    /**
     * 수입 삭제
     * @param mid 사용자 id
     * @param inid 삭제할 수입 id
     */
    @Transactional
    public void deleteIncome(int mid, int inid) {
        Member findMember = memberRepository.findById(mid).get();

        memberService.setRest(findMember, 0);

        incomeRepository.deleteById(inid);
    }

    /**
     * 수입 삭제
     * @param mid 사용자 id
     * @param exid 삭제할 지출 id
     */
    @Transactional
    public void deleteExpense(int mid, int exid) {
        Member findMember = memberRepository.findById(mid).get();

        memberService.setRest(findMember, 0);

        expenseRepository.deleteById(exid);
    }


}
