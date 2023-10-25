package com.transactioncurrency.services;

import com.transactioncurrency.domain.transaction.Transaction;
import com.transactioncurrency.dtos.TransactionDTO;
import com.transactioncurrency.dtos.TransactionResponseDTO;
import com.transactioncurrency.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repository;

    @Mock
    private FiscalDataService fiscalDataService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should save a basic transaction successfully when everything is OK")
    void createTransactionSuccess1() {
        TransactionDTO request = new TransactionDTO(
                "This is a transaction", null, BigDecimal.TEN);
        transactionService.create(request);

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should save a old transaction successfully when everything is OK")
    void createTransactionSuccess2() {
        TransactionDTO request = new TransactionDTO(
                "This is a transaction", LocalDate.of(2010,10, 10), BigDecimal.TEN);
        transactionService.create(request);

        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should round up the purchaseAmount with scale 3 successfully when everything is OK")
    void createTransactionSuccess3() {
        TransactionDTO request = new TransactionDTO(
                "This is a transaction", null, BigDecimal.valueOf(1.111));
        Transaction transaction = transactionService.create(request);

        verify(repository, times(1)).save(any());

        Assertions.assertEquals(BigDecimal.valueOf(1.11), transaction.getPurchaseAmount());
    }

    @Test
    @DisplayName("Should throw Exception when transactionDate is in the future")
    void createTransactionError1() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO request = new TransactionDTO(
                    "This is a transaction",
                    LocalDate.of(2222, 2,22),
                    BigDecimal.TEN);
            transactionService.create(request);
        });

        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Should retrive a basic transaction successfully when everything is OK")
    void getByIdAndCurrencySuccess1() {
        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.now();
        Transaction transaction = new Transaction(id, BigDecimal.TEN, "This is a transaction", date);

        when(repository.findById(id)).thenReturn(Optional.of(transaction));

        when(fiscalDataService.getByDateAndCurrency(any(), any())).thenReturn(BigDecimal.TEN);

        TransactionResponseDTO responseDTO = transactionService.getByIdAndCurrency(id, "Real");

        Assertions.assertEquals(id, responseDTO.id());
        Assertions.assertEquals(date, responseDTO.transactionDate());
        Assertions.assertEquals("This is a transaction", responseDTO.description());
        Assertions.assertEquals(BigDecimal.TEN, responseDTO.purchaseAmount());
        Assertions.assertEquals(BigDecimal.TEN, responseDTO.exchangeRate());
        Assertions.assertEquals(BigDecimal.valueOf(10000, 2), responseDTO.covertedAmount());
    }

    @Test
    @DisplayName("Should throw Exception when Id is null")
    void getByIdAndCurrencyError1() {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            transactionService.getByIdAndCurrency(null, "Brazil-Real");
        });

        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Should throw Exception when currency is null")
    void getByIdAndCurrencyError2() {
        UUID id = UUID.randomUUID();

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            transactionService.getByIdAndCurrency(id, null);
        });

        Assertions.assertNotNull(thrown);
    }

    @Test
    @DisplayName("Should throw Exception when currency is blank")
    void getByIdAndCurrencyError3() {
        UUID id = UUID.randomUUID();

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            transactionService.getByIdAndCurrency(id, "");
        });

        Assertions.assertNotNull(thrown);
    }
}