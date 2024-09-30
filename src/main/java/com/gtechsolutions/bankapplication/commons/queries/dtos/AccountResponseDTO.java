package com.gtechsolutions.bankapplication.commons.queries.dtos;

import com.gtechsolutions.bankapplication.commons.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private String accountId;
    private double amount;
    private AccountStatus status;
}
