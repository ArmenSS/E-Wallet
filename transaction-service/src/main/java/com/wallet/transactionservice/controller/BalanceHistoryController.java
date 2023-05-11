package com.wallet.transactionservice.controller;

import com.wallet.transactionservice.dto.BalanceHistoryDto;
import com.wallet.transactionservice.service.BalanceHistoryService;
import com.wallet.userservice.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/balance-history")
public class BalanceHistoryController {

    private final BalanceHistoryService balanceHistoryService;

    /**
     * Retrieves the total balances for every hour.
     *
     * @param currentUser The authenticated user.
     * @return ResponseEntity with the list of balance history information.
     */
    @GetMapping
    public ResponseEntity<List<BalanceHistoryDto>> findTotalBalancesForEveryHour(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(balanceHistoryService.findTotalBalancesForEveryHour(currentUser.getUser()));
    }

}
