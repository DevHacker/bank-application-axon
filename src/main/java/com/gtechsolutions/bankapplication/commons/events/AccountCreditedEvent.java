package com.gtechsolutions.bankapplication.commons.events;

import com.gtechsolutions.bankapplication.commons.enums.AccountStatus;
import lombok.Getter;

public class AccountCreditedEvent extends BaseEvent<String> {
    @Getter
    private String currency;
    @Getter
    private Double amount;


    public AccountCreditedEvent(String id, String currency, Double amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}
