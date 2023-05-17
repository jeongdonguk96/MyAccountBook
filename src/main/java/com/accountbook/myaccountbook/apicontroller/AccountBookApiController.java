package com.accountbook.myaccountbook.apicontroller;

import com.accountbook.myaccountbook.domain.Expense;
import com.accountbook.myaccountbook.domain.Income;
import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.accountbook.ExpenseWriteDto;
import com.accountbook.myaccountbook.dto.accountbook.IncomeWriteDto;
import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/accountBook")
@RequiredArgsConstructor
public class AccountBookApiController {

    private final AccountBookService accountBookService;


    // 수입 작성
    @PostMapping("/writeIncome")
    public ResponseDto<Integer> writeIncome(@RequestBody IncomeWriteDto incomeWriteDto) {
        accountBookService.writeIncome(incomeWriteDto);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }


    // 지출 작성
    @PostMapping("/writeExpense")
    public ResponseDto<Integer> writeExpense(@RequestBody ExpenseWriteDto expenseWriteDto) {
        accountBookService.writeExpense(expenseWriteDto);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }


    // 수입 수정
    @PutMapping("/modifyIncome/{inid}")
    public ResponseDto<Integer> modifyIncome(int mid, Income income) {
        accountBookService.modifyIncome(mid, income);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }


    // 지출 수정
    @PutMapping("/modifyExpense/{exid}")
    public ResponseDto<Integer> modifyExpense(int mid, Expense expense) {
        accountBookService.modifyExpense(mid, expense);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }


    // 수입 삭제
    @DeleteMapping("/deleteIncome/{inid}")
    public ResponseDto<Integer> deleteIncome(int mid, int inid) {
        accountBookService.deleteIncome(mid, inid);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }


    // 지출 삭제
    @DeleteMapping("/modifyExpense/{exid}")
    public ResponseDto<Integer> deleteExpense(int mid, int exid) {
        accountBookService.deleteExpense(mid, exid);

        return new ResponseDto<>(HttpStatus.OK.value(), 1);
    }
}
