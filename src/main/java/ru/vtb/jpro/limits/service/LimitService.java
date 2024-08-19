package ru.vtb.jpro.limits.service;


import java.math.BigDecimal;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.jpro.limits.LimitException;
import ru.vtb.jpro.limits.dto.LimitDTO;
import ru.vtb.jpro.limits.dto.RequestDTO;
import ru.vtb.jpro.limits.dto.ResponseDTO;
import ru.vtb.jpro.limits.entity.Limit;
import ru.vtb.jpro.limits.repository.LimitRepository;

@Service
@Slf4j
public class LimitService {

    @Value("${limits.defaultDailyLimit}")
    @Getter
    private BigDecimal defaultDailyLimit;
    private final LimitRepository limitRepository;

    public LimitService(LimitRepository limitRepository) {
        this.limitRepository = limitRepository;
    }

    private Limit getLimitByClientId(Long clientId) {
        return limitRepository.findById(clientId)
                   .orElseGet(() -> {
                       Limit limit = new Limit(clientId, defaultDailyLimit, BigDecimal.valueOf(0), defaultDailyLimit);
                       limitRepository.save(limit);
                       return limit;
                   });
    }

    public LimitDTO getLimits(Long clientId) {
        Limit limit = getLimitByClientId(clientId);
        return new LimitDTO(limit.getCurrentLimit(), limit.getReservedSum(), limit.getDailyLimit());
    }

    @Transactional
    public ResponseDTO reserveSum(Long clientId, RequestDTO requestDTO) {
        Limit limit = getLimitByClientId(clientId);
        if (requestDTO.sumPay().compareTo(limit.getCurrentLimit()) > 0) {
            throw new LimitException(HttpStatus.BAD_REQUEST,
                "Превышение лимита. Текущий остаток лимита %s".formatted(limit.getCurrentLimit()));
        }
        limit.setCurrentLimit(limit.getCurrentLimit()
                                  .subtract(requestDTO.sumPay()));
        limit.setReservedSum(limit.getReservedSum()
                                 .add(requestDTO.sumPay()));
        limitRepository.save(limit);
        return new ResponseDTO(true, HttpStatus.OK, null);
    }

    @Transactional
    public ResponseDTO applyReservedSum(Long clientId, RequestDTO requestDTO) {
        Limit limit = getLimitByClientId(clientId);
        if (requestDTO.sumPay().compareTo(limit.getReservedSum()) > 0) {
            throw new LimitException(HttpStatus.BAD_REQUEST,
                "Превышение зарезервированной суммы. Текущее значение зарезервированной суммы %s".formatted(
                    limit.getReservedSum()));
        }
        limit.setReservedSum(limit.getReservedSum()
                                 .subtract(requestDTO.sumPay()));
        limitRepository.save(limit);
        return new ResponseDTO(true, HttpStatus.OK, null);
    }

    @Transactional
    public ResponseDTO revertReservedSum(Long clientId, RequestDTO requestDTO) {
        Limit limit = getLimitByClientId(clientId);
        String message = null;
        if (requestDTO.sumPay().compareTo(limit.getReservedSum()) > 0) {
            message = "Превышение зарезервированной суммы. Текущее значение зарезервированной суммы %s".formatted(
                limit.getReservedSum());
            limit.setReservedSum(BigDecimal.valueOf(0));
        } else {
            limit.setReservedSum(limit.getReservedSum()
                                     .subtract(requestDTO.sumPay()));
        }
        limit.setCurrentLimit(limit.getCurrentLimit()
                                  .add(requestDTO.sumPay()));
        limitRepository.save(limit);
        return new ResponseDTO(true, HttpStatus.OK, message);
    }

    @Scheduled(cron = "${limits.time-restore-limit}", zone = "Europe/Moscow")
    @Transactional
    public void restoreDailyLimitsForAll() {
        log.debug("Восстановление лимитов (начало)");
        limitRepository.restoreDailyLimitsForAll(defaultDailyLimit);
        log.debug("Восстановление лимитов (окончание)");
    }
}
