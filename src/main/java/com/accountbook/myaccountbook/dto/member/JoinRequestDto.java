package com.accountbook.myaccountbook.dto.member;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record JoinRequestDto(
        @NotBlank
        @Size(max = 15)
        String username,

        @NotBlank
        @Size(max = 12)
        String pwd,

        @NotBlank
        String name,

        @NotNull
        @Max(100)
        int age,

        @NotBlank
        String field,

        @NotNull
        int year,

        @NotNull
        int salary
) {
}
