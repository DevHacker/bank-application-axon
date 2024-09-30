package com.gtechsolutions.bankapplication.queries.controllers;

import com.gtechsolutions.bankapplication.commons.queries.dtos.GetAccountQueryDTO;
import com.gtechsolutions.bankapplication.commons.queries.dtos.GetAllAccountQueryDTO;
import com.gtechsolutions.bankapplication.queries.entity.Account;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account/queries")
@RequiredArgsConstructor
public class AccountQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/retrieve-account/{accountId}")
    public Account retrieveAccountById(@PathVariable String accountId) {
        return queryGateway.query(new GetAccountQueryDTO(accountId), Account.class).join();
    }

    @GetMapping("/retrieve-accounts")
    public List<Account> retrieveAllAccount() {
        return queryGateway.query(new GetAllAccountQueryDTO(), ResponseTypes.multipleInstancesOf(Account.class)).join();
    }
}
