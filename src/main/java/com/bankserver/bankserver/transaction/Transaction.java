package com.bankserver.bankserver.transaction;

import com.bankserver.bankserver.account.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    public Transaction() {}

    public Transaction(Account sourceAccount, Account destinationAccount, float amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public Transaction(Account sourceAccount, Account destinationAccount, float amount, String description) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Account sourceAccount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Account destinationAccount;

    @Column(updatable = false, nullable = false)
    private float amount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Date timestamp;

    @Column(updatable = false)
    @Size(max = 256)
    private String description;

    // getters

    public UUID getId() {
        return id;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public Account getDestinationAccount() {
        return destinationAccount;
    }

    public float getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }
}
