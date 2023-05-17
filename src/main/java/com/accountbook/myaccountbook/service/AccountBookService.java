package com.accountbook.myaccountbook.service;

import com.accountbook.myaccountbook.domain.AccountBook;
import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.domain.Member;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseWriteDto;
import com.accountbook.myaccountbook.dto.accountbook.IncomeWriteDto;
import com.accountbook.myaccountbook.repository.AccountBookRepository;
import com.accountbook.myaccountbook.repository.ExpenseRepository;
import com.accountbook.myaccountbook.repository.IncomeRepository;
import com.accountbook.myaccountbook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookRepository accountBookRepository;
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
        Income income = new Income(incomeWriteDto.getIncomeMoney(), incomeWriteDto.getIncomeReason());

        Member findMember = memberRepository.findById(incomeWriteDto.getMid()).get();
        memberService.setRest(findMember, incomeWriteDto.getIncomeMoney());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(new Date());
        String year = date.substring(0, 4);
        String month = date.substring(0,6);

        AccountBook dupAccountBook = checkDuplicate(date);

        if (dupAccountBook != null) {
            income.setAccountBook(dupAccountBook);
        } else {
            income.setAccountBook(new AccountBook(findMember, year, month, date));
        }

        incomeRepository.save(income);
    }


    /**
     * 지출 작성
     * @param expenseWriteDto 지출 작성 Dto
     */
    @Transactional
    public void writeExpense(ExpenseWriteDto expenseWriteDto) {
        Expense expense = new Expense(expenseWriteDto.getExpenseMoney(), expenseWriteDto.getExpenseReason(), expenseWriteDto.getExpenseCategory());

        Member findMember = memberRepository.findById(expenseWriteDto.getMid()).get();
        memberService.setRest(findMember, -expenseWriteDto.getExpenseMoney());

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(new Date());
        String year = date.substring(0, 4);
        String month = date.substring(0,6);

        AccountBook dupAccountBook = checkDuplicate(date);

        if (dupAccountBook != null) {
            expense.setAccountBook(dupAccountBook);
        } else {
            expense.setAccountBook(new AccountBook(findMember, year, month, date));
        }

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

        findIncome.setIncomeReason(income.getIncomeReason());
        findIncome.setIncomeMoney(income.getIncomeMoney());

        memberService.setRest(findMember, findIncome.getIncomeMoney());
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

        findExpense.setExpenseReason(expense.getExpenseReason());
        findExpense.setExpenseMoney(expense.getExpenseMoney());

        memberService.setRest(findMember, findExpense.getExpenseMoney());
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


    /**
     * 가계부 전체 조회
     * @param mid 사용자 id
     * @return 사용자별 가계부 전체 List
     */
    @Transactional
    public List<AccountBook> findAllAccountBook(int mid) {
        return accountBookRepository.findAllByMemberMid(mid);
    }


    /**
     * 수입 전체 조회
     * @param mid 사용자 id
     * @return 사용자별 수입 전체 List
     */
    @Transactional
    public List<Income> findAllIncome(int mid) {
        return incomeRepository.findAllByAccountBookMemberMid(mid);
    }


    /**
     * 지출 전체 조회
     * @param mid 사용자 id
     * @return 사용자별 지출 전체 List
     */
    @Transactional
    public List<Expense> findAllExpense(int mid) {
        return expenseRepository.findAllByAccountBookMemberMid(mid);
    }


    /**
     * 동일한 날짜로 작성된 가계부가 있는지 체크
     * @param date 작성 날짜
     * @return 있으면 해당 AccountBook 객체, 없으면 null
     */
    @Transactional
    public AccountBook checkDuplicate(String date) {
        return accountBookRepository.findByDate(date);
    }
}
