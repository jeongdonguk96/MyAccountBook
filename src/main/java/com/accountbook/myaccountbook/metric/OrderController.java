package com.accountbook.myaccountbook.metric;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @Timed("my.order")
    @Counted("my.order")
    @GetMapping("/purchase")
    public void purchase() {
        orderService.increase();
        sleep(500);
    }


    @Timed("my.order")
    @Counted("my.order")
    @GetMapping("/cancel")
    public void cancel() {
        orderService.decrease();
        sleep(150);
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis + new Random().nextInt(200));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
