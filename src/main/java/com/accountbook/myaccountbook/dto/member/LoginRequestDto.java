package com.accountbook.myaccountbook.dto.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank
        @Size(max = 20)
        String username,

        @NotBlank
        @Size(max = 12)
        String pwd
) {
}
