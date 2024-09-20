package com.accountbook.myaccountbook.metric;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    public void increase() {
        log.info("구매 완료");
    }

    public void decrease() {
        log.info("취소 완료");
    }

}
