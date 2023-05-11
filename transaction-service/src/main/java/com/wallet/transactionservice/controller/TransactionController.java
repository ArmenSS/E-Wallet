package com.wallet.transactionservice.controller;

import com.wallet.transactionservice.dto.TransactionDto;
import com.wallet.transactionservice.entity.Transaction;
import com.wallet.transactionservice.service.TransactionService;
import com.wallet.userservice.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Adds a new transaction.
     *
     * @param currentUser The authenticated user.
     * @param transaction The transaction to be added.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping
    public ResponseEntity<Void> addTransaction(@AuthenticationPrincipal CurrentUser currentUser, @RequestBody Transaction transaction) {
        transactionService.addTransaction(currentUser.getUser(), transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves a transaction by its ID.
     *
     * @param id The ID of the transaction.
     * @return ResponseEntity with the transaction information.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        TransactionDto transactionDto = transactionService.findById(id);
        return ResponseEntity.ok(transactionDto);
    }

    /**
     * Deletes a transaction by its ID.
     *
     * @param id The ID of the transaction.
     * @return ResponseEntity indicating the success of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates a transaction.
     *
     * @param transactionDto The updated transaction information.
     * @return ResponseEntity with the updated transaction information.
     */
    @PutMapping
    public ResponseEntity<TransactionDto> updateTransaction(@RequestBody TransactionDto transactionDto) {
        TransactionDto update = transactionService.update(transactionDto);
        return ResponseEntity.ok(update);
    }

    /**
     * Retrieves all transactions for the authenticated user.
     *
     * @param currentUser The authenticated user.
     * @return ResponseEntity with the list of transactions.
     */
    @GetMapping("/findAll")
    public ResponseEntity<List<TransactionDto>> findAll(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(transactionService.findAll(currentUser.getUser()));
    }

    /**
     * Performs a deposit transaction.
     *
     * @param currentUser The authenticated user.
     * @param cardId      The ID of the card to deposit to.
     * @param amount      The amount to deposit.
     */
    @PostMapping("/deposit")
    public void deposit(@AuthenticationPrincipal CurrentUser currentUser,
                        @RequestParam Long cardId,
                        @RequestParam double amount) {
        transactionService.deposit(currentUser.getUser(), cardId, amount);
    }

    /**
     * Performs a transfer transaction.
     *
     * @param currentUser The authenticated user.
     * @param toUser      The ID of the user to transfer to.
     * @param amount      The amount to transfer.
     */
    @PostMapping("/transfer")
    public void transfer(@AuthenticationPrincipal CurrentUser currentUser,
                         @RequestParam Long toUser,
                         @RequestParam double amount) {
        transactionService.transfer(currentUser.getUser(), toUser, amount);
    }

    /**
     * Performs a withdrawal transaction.
     *
     * @param currentUser The authenticated user.
     * @param amount      The amount to withdraw.
     */
    @PostMapping("/withdraw")
    public void withdraw(@AuthenticationPrincipal CurrentUser currentUser,
                         @RequestParam double amount) {
        transactionService.withdraw(currentUser.getUser(), amount);
    }

    /**
     * Retrieves the transaction history for the authenticated user.
     *
     * @param currentUser The authenticated user.
     * @return ResponseEntity with the list of transaction history.
     */
    @GetMapping("/myHistory")
    public ResponseEntity<List<TransactionDto>> seeMyHistory(@AuthenticationPrincipal CurrentUser currentUser) {
        List<TransactionDto> transactionDtos = transactionService.seeMyHistory(currentUser.getUser());
        return ResponseEntity.ok(transactionDtos);
    }
}
