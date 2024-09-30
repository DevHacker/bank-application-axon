package com.gtechsolutions.bankapplication.commons.queries.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAccountQueryDTO {
    private String accountId;
}
