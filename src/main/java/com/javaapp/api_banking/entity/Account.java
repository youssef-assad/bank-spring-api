package com.javaapp.api_banking.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(nullable = false)
    private BigDecimal balance;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    @PrePersist
    public void onOpen() {

        this.openedAt = LocalDateTime.now();
        this.balance = this.balance == null ? BigDecimal.ZERO :this.balance;
        this.status=AccountStatus.ACTIVE;
    }
}
