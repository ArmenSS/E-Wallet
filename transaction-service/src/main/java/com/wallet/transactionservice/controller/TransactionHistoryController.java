package com.wallet.transactionservice.controller;

import com.wallet.transactionservice.dto.TransactionHistoryDto;
import com.wallet.transactionservice.service.TransactionHistoryService;
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
@RequestMapping("/transaction-history")
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    /**
     * Retrieves the total transaction counts for the last hours.
     *
     * @param currentUser The authenticated user.
     * @return ResponseEntity with the list of transaction history information.
     */
    @GetMapping
    public ResponseEntity<List<TransactionHistoryDto>> findTotalBalancesForEveryHour(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(transactionHistoryService.findTransactionsCountForLastHours(currentUser.getUser()));
    }

}
