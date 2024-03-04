package com.accountbook.myaccountbook.dto.accountbook;

import com.accountbook.myaccountbook.entity.Income;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class IncomeReturnDto {

    @NotNull
    private int inid;

    @NotNull
    private int incomeMoney;

    @NotBlank
    private String incomeReason;

    @NotBlank
    private String month;

    // DB에서 조회한 엔티티를 Dto로 변환한다.
    public static IncomeReturnDto convertToDto(Income income) {
        IncomeReturnDto incomeReturnDto = new IncomeReturnDto();
        incomeReturnDto.setInid(income.getInid());
        incomeReturnDto.setIncomeMoney(income.getIncomeMoney());
        incomeReturnDto.setIncomeReason(income.getIncomeReason());
        incomeReturnDto.setMonth(income.getMonth());

        return incomeReturnDto;
    }
}
