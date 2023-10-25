package com.transactioncurrency.dtos;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record TransactionDTO(String description, LocalDate transactionDate, @NotNull BigDecimal purchaseAmount) {
}
