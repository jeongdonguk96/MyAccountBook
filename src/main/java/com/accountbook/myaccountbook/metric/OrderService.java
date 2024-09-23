package com.accountbook.myaccountbook.metric;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    private int stock = 100;

    public void increase() {
        log.info("구매 완료");
        this.stock -= 1;
    }

    public void decrease() {
        log.info("취소 완료");
        this.stock += 1;
    }

    public int getStock() {
        return this.stock;
    }

}
