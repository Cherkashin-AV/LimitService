package ru.vtb.jpro.limits.repository;


import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.vtb.jpro.limits.entity.Limit;


@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {
    @Query("UPDATE Limit l SET l.currentLimit = :dailyLimit, l.dailyLimit = :dailyLimit, l.reservedSum = 0")
    @Modifying
    void restoreDailyLimitsForAll(BigDecimal dailyLimit);

    @Query("SELECT MAX(l.clientId) FROM Limit l")
    Optional<Long> getMaxClientId();
}
