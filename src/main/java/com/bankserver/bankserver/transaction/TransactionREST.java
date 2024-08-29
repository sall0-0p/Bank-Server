package com.bankserver.bankserver.transaction;

import com.bankserver.bankserver.account.Account;
import com.bankserver.bankserver.account.AccountRepository;
import com.bankserver.bankserver.user.User;
import com.bankserver.bankserver.utils.server.Server;
import com.bankserver.bankserver.utils.server.ServerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TransactionREST {
    private final TransactionRepository transactionRepository;
    private final ServerRepository serverRepository;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public TransactionREST(TransactionRepository transactionRepository, ServerRepository serverRepository, TransactionService transactionService, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.serverRepository = serverRepository;
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/api/transaction/{id}")
    public ResponseEntity<?> getTransaction(@RequestHeader("X-API-KEY") String apiKey, @PathVariable("id") String id) {
        // API Auth
        Transaction transaction = transactionRepository.findById(UUID.fromString(id)).orElse(null);
        if (transaction == null) {
            return null;
        }

        Account sourceAccount = transaction.getSourceAccount();
        User sender = sourceAccount.getOwner();

        Server server = serverRepository.findById(sender.getWorldUUID()).orElse(null);
        if (server == null || !server.getApiKey().equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Key");
        }

        // Actual code
        return ResponseEntity.ok(transaction);
    }

    // POST
    // Create transaction
    // sourceAccountId (string) - id of an account to send from
    // destinationAccountId (string) - id of an account to receive to
    // amount (float) - amount to send
    // description (string, optional) - description for it
    @PostMapping("/api/transaction")
    public ResponseEntity<?> createTransaction(@RequestHeader("X-API-KEY") String apiKey, @RequestBody Map<String, Object> data) {
        if (!data.containsKey("sourceAccountId")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source account id must be provided");
        }

        if (!data.containsKey("destinationAccountId")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Destination account id must be provided");
        }

        if (!data.containsKey("amount")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be provided");
        }

        Account sourceAccount = accountRepository.findById(data.get("sourceAccountId").toString()).orElse(null);
        Account destinationAccount = accountRepository.findById(data.get("destinationAccountId").toString()).orElse(null);
        float amount = Float.parseFloat(data.get("amount").toString());

        if (sourceAccount == null || destinationAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid source account or destination account");
        }

        Transaction transaction;
        if (data.containsKey("description")) {
            try {
                transaction = transactionService.createTransaction(sourceAccount, destinationAccount, amount, data.get("description").toString());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        } else {
            try {
                transaction = transactionService.createTransaction(sourceAccount, destinationAccount, amount);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
}
