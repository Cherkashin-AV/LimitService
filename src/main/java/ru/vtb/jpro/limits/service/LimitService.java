package ru.vtb.jpro.limits.service;


import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
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

    @Getter
    private BigDecimal defaultDailyLimit;
    private final LimitRepository limitRepository;
    private final SettingService settingService;

    public LimitService(LimitRepository limitRepository, SettingService settingService) {
        this.limitRepository = limitRepository;
        this.settingService = settingService;
        defaultDailyLimit = settingService.getDefaultDailyLimit();
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

    public BigDecimal getDefaultDailyLimit(){
        return defaultDailyLimit;
    }

    @Value("#{'${limits.addressesToChangeLimit}'.split(',')}")
    List<String> listAllowedAddresses;

    @Transactional
    public ResponseDTO setDefaultDailyLimit(BigDecimal newLimit, SecurityContext context) {
        String remoteAddr = ((WebAuthenticationDetails)context.getAuthentication().getDetails()).getRemoteAddress();
        String userName = context.getAuthentication().getName();

        if (!context.getAuthentication().isAuthenticated()){
            throw new SessionAuthenticationException("Запрос на изменение лимита от неавтризованного пользователя. Запрос не выполнен.");
        }
        if (!listAllowedAddresses.contains(remoteAddr)){
            throw new SessionAuthenticationException("Запрос с неразрешенного адреса. Запрос не выполнен.");
        }
        log.warn("Пользователь %s (адрес: %s) изменил ежедневный лимит. Старое значение: %s, новое значение: %s",userName, remoteAddr, defaultDailyLimit, newLimit);
        defaultDailyLimit = newLimit;
        settingService.setDefaultDailyLimit(newLimit);
        return new ResponseDTO(true, HttpStatus.OK, null);
    }

    @Scheduled(cron = "${limits.time-restore-limit}", zone = "Europe/Moscow")
    @Transactional
    public void restoreDailyLimitsForAll() {
        log.debug("Восстановление лимитов (начало)");
        limitRepository.restoreDailyLimitsForAll(defaultDailyLimit);
        log.debug("Восстановление лимитов (окончание)");
    }
}
