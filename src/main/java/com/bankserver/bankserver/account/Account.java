package com.bankserver.bankserver.account;

import com.bankserver.bankserver.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name="accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Account {

    public Account() {}

    public Account(User owner) {
        this.owner = owner;
    }

    public Account(User owner, String displayName) {
        this.displayName = displayName;
    }

    @Id
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public long getId() {
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

    // misc methods

}
