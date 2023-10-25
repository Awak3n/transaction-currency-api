package com.transactioncurrency.repositories;

import com.transactioncurrency.domain.transaction.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;

    @Test
    @DisplayName("Should create a basic transaction successfully when everything is OK")
    void createTransactionSuccess1() {
        Transaction request = Transaction.builder()
                .description("This is a transaction")
                .purchaseAmount(BigDecimal.TEN)
                .transactionDate(LocalDate.now())
                .build();

        Transaction newTransaciton = repository.save(request);

        Assertions.assertNotNull(newTransaciton.getId());
        Assertions.assertNotNull(newTransaciton.getTransactionDate());
        Assertions.assertEquals("This is a transaction", newTransaciton.getDescription());
        Assertions.assertEquals(BigDecimal.TEN, newTransaciton.getPurchaseAmount());
    }

    @Test
    @DisplayName("Should create a transaction without description successfully when everything is OK")
    void createTransactionSuccess2() {
        Transaction request = Transaction.builder()
                .purchaseAmount(BigDecimal.TEN)
                .transactionDate(LocalDate.now())
                .build();

        Transaction newTransaciton = repository.save(request);

        Assertions.assertNotNull(newTransaciton.getId());
        Assertions.assertNull(newTransaciton.getDescription());
    }

    @Test
    @DisplayName("Should throw Exception when description is very long")
    void createTransactionError1() {
        Assertions.assertThrows(Exception.class, () -> {
            Transaction request = Transaction.builder()
                    .description("This is a very long description that exceeds the maximum length allowed")
                    .purchaseAmount(BigDecimal.TEN)
                    .transactionDate(LocalDate.now())
                    .build();

            repository.save(request);
        });
    }

    @Test
    @DisplayName("Should throw Exception when purchaseAmount is null")
    void createTransactionError2() {
        Assertions.assertThrows(Exception.class, () -> {
            Transaction request = Transaction.builder()
                    .description("This is a very long description that exceeds the maximum length allowed")
                    .purchaseAmount(null)
                    .transactionDate(LocalDate.now())
                    .build();

            repository.save(request);
        });
    }

    @Test
    @DisplayName("Should throw Exception when purchaseAmount is negative")
    void createTransactionError3() {
        Assertions.assertThrows(Exception.class, () -> {
            Transaction request = Transaction.builder()
                    .description("This is a very long description that exceeds the maximum length allowed")
                    .purchaseAmount(BigDecimal.valueOf(-1))
                    .transactionDate(LocalDate.now())
                    .build();

            repository.save(request);
        });
    }

    @Test
    @DisplayName("Should throw Exception when purchaseAmount has scale 3")
    void createTransactionError4() {
        Assertions.assertThrows(Exception.class, () -> {
            Transaction request = Transaction.builder()
                    .description("This is a very long description that exceeds the maximum length allowed")
                    .purchaseAmount(BigDecimal.valueOf(1.111))
                    .transactionDate(LocalDate.now())
                    .build();

            repository.save(request);
        });
    }

}