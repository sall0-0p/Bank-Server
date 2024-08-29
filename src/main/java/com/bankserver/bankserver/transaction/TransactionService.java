package com.bankserver.bankserver.transaction;

import com.bankserver.bankserver.account.Account;
import com.bankserver.bankserver.account.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // creating transaction
    @Transactional
    public Transaction createTransaction(Account sourceAccount, Account destinationAccount, float amount) throws Exception {
        Transaction transaction = new Transaction(sourceAccount, destinationAccount, amount);

        performTransaction(transaction);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction createTransaction(Account sourceAccount, Account destinationAccount, float amount, String description) throws Exception {
        Transaction transaction = new Transaction(sourceAccount, destinationAccount, amount, description);
        
        performTransaction(transaction);
        return transactionRepository.save(transaction);
    }

    // utils
    
    private void performTransaction(Transaction transaction) throws Exception {
        Account sourceAccount = transaction.getSourceAccount();
        Account destinationAccount = transaction.getDestinationAccount();
        float amount = getAmountAndPerformChecks(transaction, sourceAccount, destinationAccount);

        if (sourceAccount.getBalance() - amount < 0) {
            throw new Exception("Source has not enough funds to afford transaction!");
        }
        
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }

    private static float getAmountAndPerformChecks(Transaction transaction, Account sourceAccount, Account destinationAccount) throws Exception {
        float amount = transaction.getAmount();

        if (sourceAccount.equals(destinationAccount)) {
            throw new Exception("Source and destination accounts cannot be the same!");
        }

        if (sourceAccount.isSuspended() || sourceAccount.isDeleted()) {
            throw new Exception("Suspended or deleted accounts are not allowed in transactions!");
        }

        if (destinationAccount.isSuspended() || destinationAccount.isDeleted()) {
            throw new Exception("Suspended or deleted accounts are not allowed in transactions!");
        }

        if (!sourceAccount.getOwner().getWorldUUID().equals(destinationAccount.getOwner().getWorldUUID())) {
            throw new Exception("Source and destination accounts have to belong to the same server!");
        }

        if (amount < 0) {
            throw new Exception("Amount must be greater than zero!");
        }
        return amount;
    }
}
