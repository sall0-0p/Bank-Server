package com.bankserver.bankserver.user;

import com.bankserver.bankserver.account.Account;
import com.bankserver.bankserver.utils.server.Server;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "users")
public class User {

    public User() {}

    public User(UUID minecraftUUID, String username, UUID worldUUID) {
        this.minecraftUUID = minecraftUUID;
        this.username = username;
        this.worldUUID = worldUUID;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull(message = "OwnerUIID cannot be null")
    private UUID minecraftUUID;

    @Column(nullable = false)
    @NotNull(message = "Server user belongs must be defined!")
    @JsonIgnore
    private UUID worldUUID;

    @Column(nullable = false)
    @NotNull(message = "Username has to be defined")
    private String username;

    @Column(nullable = false)
    private boolean suspended;

    @Column(nullable = false)
    private int accountLimit;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Account personalAccount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedAt;

    public UUID getId() {
        return id;
    }

    public UUID getMinecraftUUID() {
        return minecraftUUID;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public int getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(int accountLimit) {
        this.accountLimit = accountLimit;
    }

    public Account getPersonalAccount() {
        return personalAccount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setPersonalAccount(Account personalAccount) {
        this.personalAccount = personalAccount;
    }

    public @NotNull(message = "Server user belongs must be defined!") UUID getWorldUUID() {
        return worldUUID;
    }
}
