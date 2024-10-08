package com.bankserver.bankserver.account;

import com.bankserver.bankserver.user.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.util.Date;

@Entity
@Component
@Table(name="accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Account {

    public Account() {
    }

    public Account(String accountId, User owner) {
        this.id = accountId;
        this.owner = owner;
        this.displayName = owner.getUsername();
    }

    public Account(String accountId, User owner, String displayName) {
        this.id = accountId;
        this.owner = owner;
        this.displayName = displayName;
    }

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private float balance;

    @Column
    private boolean suspended;

    @Column
    private boolean deleted;

    @Column
    private float creditLimit;

    @Column
    private float creditPercent;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedAt;

    // getters

    public String getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public String getDisplayName() {
        return displayName;
    }

    public float getBalance() {
        return balance;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public float getCreditLimit() {
        return creditLimit;
    }

    public float getCreditPercent() {
        return creditPercent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    // setters

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setCreditLimit(float creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setCreditPercent(float creditPercent) {
        this.creditPercent = creditPercent;
    }
}
