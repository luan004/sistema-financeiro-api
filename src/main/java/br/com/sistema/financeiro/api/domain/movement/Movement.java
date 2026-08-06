package br.com.sistema.financeiro.api.domain.movement;

import br.com.sistema.financeiro.api.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movements")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String description;

    @NotNull
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Movement() {
    }

    public Movement(User creator, String description, BigDecimal amount) {
        this.creator = creator;
        this.description = description;
        this.amount = amount;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
