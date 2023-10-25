package com.transactioncurrency.services;

import com.transactioncurrency.domain.transaction.Transaction;
import com.transactioncurrency.dtos.TransactionDTO;
import com.transactioncurrency.dtos.TransactionResponseDTO;
import com.transactioncurrency.infra.util.IntegerUtil;
import com.transactioncurrency.repositories.TransactionRepository;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private FiscalDataService fiscalDataService;

    public Transaction create(@Valid TransactionDTO transactionDTO) {
        var newTransaction = new Transaction();
        BeanUtils.copyProperties(transactionDTO, newTransaction);

        if (newTransaction.getTransactionDate() == null) {
            newTransaction.setTransactionDate(LocalDate.now());
        } else if (newTransaction.getTransactionDate().isAfter(LocalDate.now())) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST.value(), "A future date is not allowed");
        }

        newTransaction.round();

        repository.save(newTransaction);

        return newTransaction;
    }

    public TransactionResponseDTO getByIdAndCurrency(@NotNull UUID id,
                                                     @NotBlank String currency) throws EntityNotFoundException,
            HttpStatusException {
        var transaction = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        BigDecimal exchangeRate = fiscalDataService.getByDateAndCurrency(
                transaction.getTransactionDate(), currency);

        BigDecimal newAmount = calculateNewAmout(transaction.getPurchaseAmount(), exchangeRate);

        return new TransactionResponseDTO(transaction, exchangeRate, newAmount);
    }

    private BigDecimal calculateNewAmout(BigDecimal oldAmount, BigDecimal exchangeRate) {
        return oldAmount.multiply(exchangeRate).setScale(IntegerUtil.TWO, RoundingMode.HALF_EVEN);
    }

}
