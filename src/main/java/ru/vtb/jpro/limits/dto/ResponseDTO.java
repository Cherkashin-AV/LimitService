package ru.vtb.jpro.limits.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;


/***
 * Результат выполнения операции
 *
 * @param result - Результат: true - если операция выполнена, иначе false
 * @param status - Код ответа (HttpStatus)
 * @param message - Сообщение об ошибке
 */

@Schema(description = "Результат выполнения операци")
public record ResponseDTO(
    @Schema(description = "Результат: true - если операция выполнена, иначе false") Boolean result,
    @Schema(description = "Код ответа (HttpStatus)") HttpStatus status,
    @Schema(description = "Сообщение об ошибке") String message) {}
