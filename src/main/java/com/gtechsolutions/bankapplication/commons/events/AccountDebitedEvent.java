package com.gtechsolutions.bankapplication.commons.events;

import lombok.Getter;

public class AccountDebitedEvent extends BaseEvent<String> {
    @Getter
    private String currency;
    @Getter
    private Double amount;

    public AccountDebitedEvent(String id, String currency, Double amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}
