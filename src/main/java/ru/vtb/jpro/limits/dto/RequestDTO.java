package ru.vtb.jpro.limits.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;


/***
 * Структура входящего запроса на изменение лимита
 *
 * @param sumPay - Сумма платежа для резервирования, списания или восстановления лимита
 */

@Schema(description = "Структура входящего запроса на изменение лимита")
public record RequestDTO(@Min(0) @Schema(description = "Сумма платежа для резервирования, списания или восстановления лимита")
                         BigDecimal sumPay) {}
