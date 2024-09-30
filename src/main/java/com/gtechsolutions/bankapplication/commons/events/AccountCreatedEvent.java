package com.gtechsolutions.bankapplication.commons.events;

import com.gtechsolutions.bankapplication.commons.enums.AccountStatus;
import lombok.Getter;

public class AccountCreatedEvent extends BaseEvent<String> {

    @Getter
    private String currency;
    @Getter
    private double balance;
    @Getter
    private AccountStatus accountStatus;
    public AccountCreatedEvent(String id, String currency, double balance, AccountStatus accountStatus) {
        super(id);
        this.currency = currency;
        this.balance = balance;
        this.accountStatus = accountStatus;
    }
}
