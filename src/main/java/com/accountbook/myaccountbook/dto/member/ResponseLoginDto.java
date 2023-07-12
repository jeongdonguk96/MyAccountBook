package com.accountbook.myaccountbook.dto.member;

import com.accountbook.myaccountbook.userdetails.CustomUserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseLoginDto {
    private String username;
    private String pwd;

    public ResponseLoginDto(CustomUserDetails userDetails) {
        this.username = userDetails.getMember().getUsername();
        this.pwd = userDetails.getMember().getPwd();
    }
}
