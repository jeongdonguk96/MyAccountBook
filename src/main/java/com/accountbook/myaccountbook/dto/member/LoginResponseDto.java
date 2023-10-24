package com.accountbook.myaccountbook.dto.member;

import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDto {
    private String username;
    private String pwd;

    public LoginResponseDto(CustomUserDetails userDetails) {
        this.username = userDetails.getMember().getUsername();
        this.pwd = userDetails.getMember().getPwd();
    }
}
