package com.accountbook.myaccountbook.dto.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @NotBlank
    @Size(max = 20)
    private String loginId;

    @NotBlank
    @Size(max = 12)
    private String pwd;
}
