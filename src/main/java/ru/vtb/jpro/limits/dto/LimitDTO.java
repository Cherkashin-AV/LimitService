package ru.vtb.jpro.limits.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;


/***
 * Текущий лимит клиента
 *
 * @param currentLimit - Остаток лимита на текущий момент
 * @param reservedSum - Зарезервированная сумма
 * @param dailyLimit - Дневной лимит
 */

@Schema(description = "Текущий лимит клиента")
public record LimitDTO(@NotBlank @Schema(description = "Остаток лимита на текущий момент")
                       BigDecimal currentLimit,
                       @NotBlank @Schema(description = "Зарезервированная сумма")
                       BigDecimal reservedSum,
                       @NotBlank @Schema(description = "Дневной лимит")
                       BigDecimal dailyLimit) {}
