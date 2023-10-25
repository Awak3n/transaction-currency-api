package com.transactioncurrency.dtos;

import com.transactioncurrency.domain.transaction.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TransactionResponseDTO(UUID id, String description, LocalDate transactionDate,
                                     BigDecimal purchaseAmount, BigDecimal exchangeRate, BigDecimal covertedAmount) {

    public TransactionResponseDTO(Transaction transaction, BigDecimal exchangeRate, BigDecimal covertedAmount) {
        this(transaction.getId(), transaction.getDescription(), transaction.getTransactionDate(),
                transaction.getPurchaseAmount(), exchangeRate, covertedAmount);
    }
}
