package com.gtechsolutions.bankapplication.commands.controllers;

import com.gtechsolutions.bankapplication.commands.dtos.CreateAccountRequestDTO;
import com.gtechsolutions.bankapplication.commands.dtos.CreditAccountRequestDTO;
import com.gtechsolutions.bankapplication.commands.dtos.DebitAccountRequestDTO;
import com.gtechsolutions.bankapplication.commons.commands.CreateAccountCommand;
import com.gtechsolutions.bankapplication.commons.commands.CreditAccountCommand;
import com.gtechsolutions.bankapplication.commons.commands.DebitAccountCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/account")
public class AccountCommandController {

    private final CommandGateway commandGateway;
    private final EventStore eventStore;

    public AccountCommandController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/create-account")
    public CompletableFuture<String> createNewBankAccount(@RequestBody CreateAccountRequestDTO createAccountRequestDTO) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                createAccountRequestDTO.getCurrency(),
                createAccountRequestDTO.getInitialBalance()));
    }

    @PostMapping("/credit-account")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO creditAccountRequestDTO) {
        return commandGateway.send(new CreditAccountCommand(
                creditAccountRequestDTO.getAccountId(),
                creditAccountRequestDTO.getCurrency(),
                creditAccountRequestDTO.getAmount())
        );
    }

    @PostMapping("/debit-account")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO debitAccountRequestDTO) {
        return commandGateway.send(new DebitAccountCommand(
                debitAccountRequestDTO.getAccountId(),
                debitAccountRequestDTO.getCurrency(),
                debitAccountRequestDTO.getAmount()
        ));
    }

    @GetMapping("/eventStore/{eventId}")
    public Stream eventStrore(@PathVariable String eventId){
        return eventStore.readEvents(eventId).asStream();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> createAccountException(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
