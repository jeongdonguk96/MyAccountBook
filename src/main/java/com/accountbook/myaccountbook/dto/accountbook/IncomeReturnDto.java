package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.entity.Income;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record IncomeReturnDto(
    @NotNull
    int inid,

    @NotNull
    int incomeMoney,

    @NotBlank
    String incomeReason,

    @NotBlank
    String month
) {

    // DB에서 조회한 엔티티를 Dto로 변환한다.
    public static IncomeReturnDto convertToDto(Income income) {
        return new IncomeReturnDto(
                income.getInid(),
                income.getIncomeMoney(),
                income.getIncomeReason(),
                income.getMonth()
        );
    }

}