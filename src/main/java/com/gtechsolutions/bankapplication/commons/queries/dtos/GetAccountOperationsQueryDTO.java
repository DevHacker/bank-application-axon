package com.gtechsolutions.bankapplication.commons.queries.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountOperationsQueryDTO {
    private String accountId;
}
