package com.wallet.transactionservice.controller;

import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.entity.Balance;
import com.wallet.transactionservice.service.BalanceService;
import com.wallet.userservice.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;

    /**
     * Adds balance for a user with the given userId.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity with HTTP status CREATED if the balance is successfully added.
     */
    @PostMapping("/{userId}")
    public ResponseEntity<Void> addBalance(@PathVariable Long userId) {
        balanceService.addBalance(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves the balance of the authenticated user.
     *
     * @param currentUser The authenticated user.
     * @return ResponseEntity with the optional balance information.
     */
    @GetMapping("/myBalance")
    public ResponseEntity<Optional<BalanceDto>> seeMyBalance(@AuthenticationPrincipal CurrentUser currentUser) {
        Optional<Balance> balance = balanceService.seeMyBalance(currentUser.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves the balances of the users with the given ID and the authenticated user.
     *
     * @param currentUser The authenticated user.
     * @param id          The ID of the user.
     * @return ResponseEntity with the list of balance information.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<List<BalanceDto>> findAllByUsersAndBalance(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable Long id) {
        return ResponseEntity.ok(balanceService.findAllByUsersAndBalance(currentUser.getUser(), id));
    }

    /**
     * Retrieves the balance information for the balance with the given ID.
     *
     * @param id The ID of the balance.
     * @return ResponseEntity with the balance information.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BalanceDto> getBalanceById(@PathVariable Long id) {
        BalanceDto balanceDto = balanceService.findById(id);
        return ResponseEntity.ok(balanceDto);
    }

    /**
     * Deletes the balance with the given ID.
     *
     * @param id The ID of the balance.
     * @return ResponseEntity with HTTP status NO_CONTENT if the balance is successfully deleted.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBalance(@PathVariable Long id) {
        balanceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the balance information.
     *
     * @param balanceDto The updated balance information.
     * @return ResponseEntity with the updated balance information.
     */
    @PutMapping
    public ResponseEntity<BalanceDto> updateBalance(@RequestBody BalanceDto balanceDto) {
        BalanceDto update = balanceService.update(balanceDto);
        return ResponseEntity.ok(update);
    }

    /**
     * Retrieves the balances of the authenticated user.
     *
     * @param currentUser The authenticated user.
     * @return ResponseEntity with the list of balance information.
     */
    @GetMapping("/findAll")
    public ResponseEntity<List<BalanceDto>> findAll(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(balanceService.findAll(currentUser.getUser()));

    }
}
