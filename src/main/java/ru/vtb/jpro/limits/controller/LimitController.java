package ru.vtb.jpro.limits.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.jpro.limits.LimitException;
import ru.vtb.jpro.limits.dto.LimitDTO;
import ru.vtb.jpro.limits.dto.RequestDTO;
import ru.vtb.jpro.limits.dto.ResponseDTO;
import ru.vtb.jpro.limits.service.LimitService;


@RestController
@RequestMapping(path = "/v1")
@Tag(name = "Контроллер изменения лимита",
    description = "До проведения платежа необходимо зарезервировать сумму, после выполнения платежа списать зарезервированную сумму или отменить резерв в случае ошибки при проведении платежа.")
public class LimitController {

    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @Operation(summary = "Информация по лимитам", description = "Возвращает состояние лимита клиента на текущий момент")
    @GetMapping("/{clientId}")
    public LimitDTO getLimits(
        @PathVariable
        @Parameter(description = "Идентификатор клиента")
        Long clientId) {
        return limitService.getLimits(clientId);
    }

    @Operation(summary = "Зарезервировать сумму")
    @PostMapping("/{clientId}")
    public ResponseDTO reserveSum(
        @PathVariable
        @Parameter(description = "Идентификатор клиента")
        Long clientId,
        @RequestBody
        @Parameter(description = "Сумма платежа")
        RequestDTO requestDTO) {
        return limitService.reserveSum(clientId, requestDTO);
    }

    @Operation(summary = "Списать зарезервированную сумму")
    @PutMapping("/{clientId}")
    public ResponseDTO applyReservedSum(
        @PathVariable
        @Parameter(description = "Идентификатор клиента")
        Long clientId,
        @RequestBody
        @Parameter(description = "Сумма платежа")
        RequestDTO requestDTO) {
        return limitService.applyReservedSum(clientId, requestDTO);
    }

    @Operation(summary = "Отменить резервирование суммы")
    @DeleteMapping("/{clientId}")
    public ResponseDTO revertReservedSum(
        @PathVariable
        @Parameter(description = "Идентификатор клиента")
        Long clientId,
        @RequestBody
        @Parameter(description = "Сумма платежа")
        RequestDTO requestDTO) {
        return limitService.revertReservedSum(clientId, requestDTO);
    }

    @Operation(summary = "Изменить сумму дневного лимита по умолчанию")
    @PutMapping("/setDefaultDailyLimit")
    public ResponseDTO setDefaultDailyLimit(
        @RequestParam(name = "limit")
        @Parameter(description = "Идентификатор клиента")
        BigDecimal newLimit) {
        return limitService.setDefaultDailyLimit(newLimit);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO exceptionHandle(LimitException ex) {
        return new ResponseDTO(false, ex.getStatus(), ex.getMessage());
    }
}
