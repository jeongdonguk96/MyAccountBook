package com.accountbook.myaccountbook.metric;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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
    private final MeterRegistry registry;


    @Counted("my.order")
    @GetMapping("/purchase")
    public void purchase() {
        Timer timer = Timer.builder("my.order")
                .tag("class", this.getClass().getName())
                .tag("method", "purchase")
                .description("구매")
                .register(registry);

        timer.record(() -> {
            orderService.increase();
            sleep(500);
        });
    }


    @Counted("my.order")
    @GetMapping("/cancel")
    public void cancel() {
        Timer timer = Timer.builder("my.order")
                .tag("class", this.getClass().getName())
                .tag("method", "cancel")
                .description("취소")
                .register(registry);

        timer.record(() -> {
            orderService.decrease();
            sleep(150);
        });
    }


    private void sleep(int millis) {
        try {
            Thread.sleep(millis + new Random().nextInt(200));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
