package com.transactioncurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.transactioncurrency")
public class TransactioncurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactioncurrencyApplication.class, args);
    }

}
