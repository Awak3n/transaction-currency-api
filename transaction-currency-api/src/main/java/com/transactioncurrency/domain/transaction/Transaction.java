package com.transactioncurrency.domain.transaction;

import com.transactioncurrency.infra.util.IntegerUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Column(name = "purchase_amount")
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(message = "Purchase amount must be greater or equal to 0.00", value = "0.00")
    private BigDecimal purchaseAmount;

    @Column(name = "description")
    @Length(message = "Description must not exceed 50 characters!", max = 50)
    private String description;

    @NotNull
    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    public void round() {
        this.purchaseAmount = purchaseAmount.setScale(IntegerUtil.TWO, RoundingMode.HALF_EVEN);
    }

}
