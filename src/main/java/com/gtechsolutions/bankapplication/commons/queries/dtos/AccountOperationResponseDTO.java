package com.gtechsolutions.bankapplication.commons.queries.dtos;

import com.gtechsolutions.bankapplication.commons.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountOperationResponseDTO {
    private Long id;
    private Date operationDate;
    private String amount;
    private TransactionType transactionType;
}
