package com.accountbook.myaccountbook.apicontroller;

import com.accountbook.myaccountbook.dto.ResponseDto;
import com.accountbook.myaccountbook.dto.accountbook.*;
import com.accountbook.myaccountbook.service.AccountBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/accountBook")
@RequiredArgsConstructor
public class AccountBookApiController {

    private final AccountBookService accountBookService;


    // 수입을 작성한다.
    @PostMapping("/writeIncome")
    public ResponseDto<Integer> writeIncome(@RequestBody @Validated IncomeWriteDto incomeWriteDto) {
        accountBookService.writeIncome(incomeWriteDto);

        return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
    }


    // 지출을 작성한다.
    @PostMapping("/writeExpense")
    public ResponseDto<Integer> writeExpense(@RequestBody @Validated ExpenseWriteDto expenseWriteDto) {
        accountBookService.writeExpense(expenseWriteDto);

        return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
    }


    // 수입을 수정한다.
    @PutMapping("/modifyIncome/{inid}")
    public ResponseDto<Integer> modifyIncome(@RequestBody @Validated IncomeModifyDto incomeModifyDto) {
        accountBookService.modifyIncome(incomeModifyDto);

        return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
    }


    // 지출을 수정한다.
    @PutMapping("/modifyExpense/{exid}")
    public ResponseDto<Integer> modifyExpense(@RequestBody @Validated ExpenseModifyDto expenseModifyDto) {
        accountBookService.modifyExpense(expenseModifyDto);

        return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
    }


    // 수입을 삭제한다.
    @DeleteMapping("/deleteIncome/{inid}")
    public ResponseDto<Integer> deleteIncome(@RequestBody @Validated IncomeDeleteDto incomeDeleteDto) {
        accountBookService.deleteIncome(incomeDeleteDto);

        return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
    }


    // 지출을 삭제한다.
    @DeleteMapping("/deleteExpense/{exid}")
    public ResponseDto<Integer> deleteExpense(@RequestBody @Validated ExpenseDeleteDto expenseDeleteDto) {
        accountBookService.deleteExpense(expenseDeleteDto);

        return new ResponseDto<>(HttpStatus.OK.value(), null, "success");
    }
}
