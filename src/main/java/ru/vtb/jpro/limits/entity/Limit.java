package ru.vtb.jpro.limits.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "limits")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Limit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotNull
    private Long clientId;
    @Column(nullable = false)
    @Min(0)
    @NotNull
    private BigDecimal currentLimit;
    @Column(nullable = false)
    @Min(0)
    @NotNull
    private BigDecimal reservedSum;
    @Column(nullable = false)
    @Min(0)
    @NotNull
    private BigDecimal dailyLimit;

    public Limit(Long clientId, BigDecimal currentLimit, BigDecimal reservedSum, BigDecimal dailyLimit) {
        this.clientId = clientId;
        this.currentLimit = currentLimit;
        this.reservedSum = reservedSum;
        this.dailyLimit = dailyLimit;
    }
}
