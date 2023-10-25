package com.transactioncurrency.controllers;

import com.transactioncurrency.domain.transaction.Transaction;
import com.transactioncurrency.dtos.ExceptionDTO;
import com.transactioncurrency.dtos.TransactionDTO;
import com.transactioncurrency.dtos.TransactionResponseDTO;
import com.transactioncurrency.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private static final String APP_JSON = "application/json";

    @Autowired
    private TransactionService service;

    @Operation(
            summary = "Register a new transaction into the App",
            description = "Register a Transaction object by specifying its description, transaction date " +
                    "and purchase amount. The response is a full Transaction object."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", content = {
            @Content(schema = @Schema(implementation = Transaction.class), mediaType = APP_JSON) }),
        @ApiResponse(responseCode = "400", content = {
            @Content(schema = @Schema(implementation = ExceptionDTO.class), mediaType = APP_JSON) }),
        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "500", content = {
            @Content(schema = @Schema(implementation = ExceptionDTO.class), mediaType = APP_JSON) }) })
    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody TransactionDTO transaction) {
        var transactionResponse = this.service.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse);
    }

    @Operation(
            summary = "Retrieve a transaction with a new amount coverted by the currency",
            description = "Get a Transaciton object by specifying its id and currency to convert into. " +
                    "The response is a Transaction record with id, description, transaction date, purchase amount, " +
                    "exchange rate and coverted amount."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = TransactionResponseDTO.class), mediaType = APP_JSON) }),
        @ApiResponse(responseCode = "400", content = {
            @Content(schema = @Schema(implementation = ExceptionDTO.class), mediaType = APP_JSON) }),
        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "500", content = {
            @Content(schema = @Schema(implementation = ExceptionDTO.class), mediaType = APP_JSON) }) })
    @GetMapping("/{id}/currency/{currency}")
    public ResponseEntity<TransactionResponseDTO> getByIdAndCurrency(
            @PathVariable(value = "id") UUID id, @PathVariable(value = "currency") String currency) {
        var transactionResponse = this.service.getByIdAndCurrency(id, currency);

        return ResponseEntity.status(HttpStatus.OK).body(transactionResponse);
    }
}
