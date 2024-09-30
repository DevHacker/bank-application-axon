package com.gtechsolutions.bankapplication.commands.aggregates;

import com.gtechsolutions.bankapplication.commons.commands.CreateAccountCommand;
import com.gtechsolutions.bankapplication.commons.commands.CreditAccountCommand;
import com.gtechsolutions.bankapplication.commons.commands.DebitAccountCommand;
import com.gtechsolutions.bankapplication.commons.enums.AccountStatus;
import com.gtechsolutions.bankapplication.commons.events.AccountCreatedEvent;
import com.gtechsolutions.bankapplication.commons.events.AccountCreditedEvent;
import com.gtechsolutions.bankapplication.commons.events.AccountDebitedEvent;
import com.gtechsolutions.bankapplication.commons.exceptions.AccountInitialBalanceException;
import com.gtechsolutions.bankapplication.commons.exceptions.BalanceInsuffisantFundException;
import com.gtechsolutions.bankapplication.commons.exceptions.OperationNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate(type = "AccountAggregate")
@Slf4j
public class AccountAggregate {

    @AggregateIdentifier
    private String accountId;

    private String currency;

    private double balance;

    private AccountStatus status;

    public AccountAggregate() {
        //Required By Axon
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand accountCommand) {

        if (accountCommand.getId() == null) {
            throw new IllegalArgumentException("Aggregate ID cannot be null");
        }

        if(accountCommand.getInitialBalance() < 0) throw new AccountInitialBalanceException("Account initial balance cannot be negative");
        AggregateLifecycle.apply(new AccountCreatedEvent(
                accountCommand.getId(),
                accountCommand.getCurrency(),
                accountCommand.getInitialBalance(),
                AccountStatus.CREATED
        ));
        log.info("CreateAccountCommand executed successfully");
    }

    @EventSourcingHandler(payloadType = AccountCreatedEvent.class)
    public void on(AccountCreatedEvent event) {

        // Vérifiez que l'ID de l'événement n'est pas null
        if (event.getId() == null) {
            throw new IllegalStateException("Aggregate ID cannot be null after applying the creation event");
        }
        this.accountId = event.getId();
        this.balance = event.getBalance();
        this.currency = event.getCurrency();
        this.status = event.getAccountStatus();
        log.info("Aggragate AccountCreatedEvent executed successfully");
    }

    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand) {
        if (this.accountId == null) {
            throw new IllegalStateException("Account has not been created yet!");
        }
        if(creditAccountCommand.getAmount()<0 || this.status == AccountStatus.SUSPENDED) throw new OperationNotPermittedException("Account is suspended");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getCurrency(),
                creditAccountCommand.getAmount()
        ));
        log.info("CreditAccountCommand executed successfully");
    }

    @EventSourcingHandler(payloadType = AccountCreditedEvent.class)
    public void on(AccountCreditedEvent event) {

        if (event.getId() == null) {
            throw new IllegalStateException("Aggregate ID cannot be null after applying the creation event");
        }
        this.balance += event.getAmount();
        this.currency = event.getCurrency();
        this.status = AccountStatus.ACTIVATED;
        log.info("Aggregate AccountCreditedEvent executed successfully");
    }

    @CommandHandler
    public void handle(DebitAccountCommand debitAccountCommand) {

        if(this.balance < debitAccountCommand.getAmount()) throw new BalanceInsuffisantFundException("Account balance is not sufficient");
        AggregateLifecycle.apply(new AccountDebitedEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getCurrency(),
                debitAccountCommand.getAmount()
        ));
        log.info("DebitAccountCommand executed successfully");
    }

    @EventSourcingHandler(payloadType = AccountDebitedEvent.class)
    public void on(AccountDebitedEvent event) {
        this.balance -= event.getAmount();
        log.info("Aggregate AccountDebitedEvent executed successfully");
    }
}
