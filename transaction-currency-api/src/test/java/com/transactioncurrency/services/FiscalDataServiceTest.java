package com.transactioncurrency.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class FiscalDataServiceTest {

    @Autowired
    private FiscalDataService fiscalDataService;

    @Test
    @DisplayName("Should retrive a basic exchange successfully when everything is OK")
    void createTransactionSuccess1() {
        LocalDate date = LocalDate.of(2023, 9, 30);
        String currency = "Afghanistan-Afghani";

        var bigDecimal = fiscalDataService.getByDateAndCurrency(date, currency);

        Assertions.assertNotNull(bigDecimal);
        Assertions.assertInstanceOf(BigDecimal.class, bigDecimal);
    }

    @Test
    @DisplayName("Should throw Exception when no content return")
    void createTransactionError1() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            LocalDate date = LocalDate.of(2023, 9, 30);
            String currency = "Wex-Wexer";
            fiscalDataService.getByDateAndCurrency(date, currency);
        });

        Assertions.assertNotNull(thrown);
    }

}