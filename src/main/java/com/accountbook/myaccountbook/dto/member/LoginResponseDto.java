package com.accountbook.myaccountbook.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDto {
    private String username;
    private String pwd;
}
