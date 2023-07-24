package com.accountbook.myaccountbook.utils;

import com.accountbook.myaccountbook.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class CustomResponseUtil {

    // 성공 응답
    public static void success(HttpServletResponse response, Object object, String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(200, object, message);
            String responseBody = objectMapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 실패 응답
    public static void fail(HttpServletResponse response, HttpStatus httpStatus, String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(httpStatus.value(), httpStatus, message);
            String responseBody = objectMapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
