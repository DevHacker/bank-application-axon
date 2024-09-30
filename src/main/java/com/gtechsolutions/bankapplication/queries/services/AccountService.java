package com.gtechsolutions.bankapplication.queries.services;

import com.gtechsolutions.bankapplication.commons.enums.TransactionType;
import com.gtechsolutions.bankapplication.commons.events.AccountCreatedEvent;
import com.gtechsolutions.bankapplication.commons.events.AccountCreditedEvent;
import com.gtechsolutions.bankapplication.commons.events.AccountDebitedEvent;
import com.gtechsolutions.bankapplication.commons.queries.dtos.GetAccountQueryDTO;
import com.gtechsolutions.bankapplication.commons.queries.dtos.GetAllAccountQueryDTO;
import com.gtechsolutions.bankapplication.queries.entity.Account;
import com.gtechsolutions.bankapplication.queries.entity.AccountOperation;
import com.gtechsolutions.bankapplication.queries.repository.AccountOperationRepository;
import com.gtechsolutions.bankapplication.queries.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountOperationRepository operationRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public AccountService(AccountRepository accountRepository, AccountOperationRepository operationRepository, QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler(payloadType = AccountCreatedEvent.class)
    public void on(AccountCreatedEvent accountCreatedEvent, EventMessage<AccountCreatedEvent> eventMessage) {
        log.info("Event received: " + eventMessage.getPayload().toString());
        Account account = new Account();
        account.setAccountId(accountCreatedEvent.getId());
        account.setBalance(accountCreatedEvent.getBalance());
        account.setCurrency(accountCreatedEvent.getCurrency());
        account.setStatus(accountCreatedEvent.getAccountStatus());
        account.setCreatedAt(Date.from(eventMessage.getTimestamp()));
        this.accountRepository.save(account);
    }

    @EventHandler(payloadType = AccountDebitedEvent.class)
    public void on(AccountDebitedEvent accountDebitedEvent, EventMessage<AccountDebitedEvent> eventMessage) {
        log.info("Event received id : {} ", eventMessage.getPayload().getId());
        Account account = accountRepository.findById(accountDebitedEvent.getId()).orElseThrow(()->new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() - accountDebitedEvent.getAmount());
        account.setUpdatedAt(Date.from(eventMessage.getTimestamp()));

        Account savedAccount = this.accountRepository.save(account);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setCreatedAt(Date.from(eventMessage.getTimestamp()));
        accountOperation.setAmount(accountDebitedEvent.getAmount());
        accountOperation.setTransactionType(TransactionType.DEBIT);
        accountOperation.setAccount(savedAccount);
        operationRepository.save(accountOperation);
        queryUpdateEmitter.emit(GetAccountQueryDTO.class, query -> true, new GetAccountQueryDTO(savedAccount.getAccountId() ));
    }

    @EventHandler(payloadType = AccountCreditedEvent.class)
    public void on(AccountCreditedEvent accountCreditedEvent, EventMessage<AccountCreditedEvent> eventMessage) {
        log.info("Event received id : {} ", eventMessage.getPayload().getId());
        Account account = accountRepository.findById(accountCreditedEvent.getId()).orElseThrow(()->new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + accountCreditedEvent.getAmount());
        account.setUpdatedAt(Date.from(eventMessage.getTimestamp()));

        Account savedAccount = this.accountRepository.save(account);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setCreatedAt(Date.from(eventMessage.getTimestamp()));
        accountOperation.setAmount(accountCreditedEvent.getAmount());
        accountOperation.setTransactionType(TransactionType.CREDIT);
        accountOperation.setAccount(savedAccount);
        operationRepository.save(accountOperation);
        queryUpdateEmitter.emit(GetAccountQueryDTO.class, query -> true, new GetAccountQueryDTO(savedAccount.getAccountId() ));
    }

    @QueryHandler
    public Account handle(GetAccountQueryDTO getAccountQueryDTO) {
        return accountRepository.findById(getAccountQueryDTO.getAccountId())
                .orElseThrow(()->new RuntimeException("Account not found"));
    }

    @QueryHandler
    public List<Account> handle(GetAllAccountQueryDTO getAllAccountQueryDTO) {
        return accountRepository.findAll();
    }
}
