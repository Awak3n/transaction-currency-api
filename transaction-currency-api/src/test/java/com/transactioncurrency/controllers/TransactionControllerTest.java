package com.transactioncurrency.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transactioncurrency.dtos.TransactionDTO;
import com.transactioncurrency.dtos.TransactionResponseDTO;
import com.transactioncurrency.infra.handler.ControllerExceptionHandler;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.transactioncurrency.services.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private JacksonTester<TransactionDTO> jsonTransaction;

    @BeforeEach
    void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should return CREATED when everything is OK")
    void createTransactionReturnCreated() throws Exception {
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .description("This is a transaction")
                .purchaseAmount(BigDecimal.TEN)
                .transactionDate(LocalDate.now())
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                post("/transaction/").contentType(MediaType.APPLICATION_JSON).content(
                        jsonTransaction.write(transactionDTO).getJson()
                )).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    @DisplayName("Should return OK when everything is OK")
    void getTransactionByIdAndCurrencyReturnOk() throws Exception {
        UUID uuid = UUID.randomUUID();
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO(
                uuid, "This is a description", LocalDate.now(), BigDecimal.ONE, BigDecimal.TEN,
                BigDecimal.TEN);

        given(transactionService.getByIdAndCurrency(uuid, "Brazil-Real"))
                .willReturn(transactionResponseDTO);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/transaction/%s/currency/%s".formatted(uuid, "Brazil-Real"))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

}