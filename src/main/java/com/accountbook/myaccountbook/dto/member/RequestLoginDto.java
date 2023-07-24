package com.accountbook.myaccountbook.dto.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RequestLoginDto {

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 12)
    private String pwd;

}
