package by.lapil.audioplayer.advice;

import by.lapil.audioplayer.exception.ValidationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(field ->
                errors.put(field.getField(), field.getDefaultMessage())
        );
        log.warn("Validation errors: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleCustomValidation(ValidationException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Ошибка чтения тела запроса");
        body.put("message", ex.getMessage());

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();

            String fieldName = ife.getPath().get(0).getFieldName();
            String invalidValue = String.valueOf(ife.getValue());

            if (targetType.isEnum()) {
                Object[] allowed = targetType.getEnumConstants();

                String allowedValues = String.join(", ",
                        java.util.Arrays.stream(allowed).map(Object::toString).toList());

                log.warn("[VALIDATION] Некорректное значение '{}' для поля '{}'. Разрешено: {}",
                        invalidValue, fieldName, allowedValues);

                body.put("message", "Некорректное значение в поле " + fieldName + ": '" + invalidValue +
                        "'. Разрешённые значения: [" + allowedValues + "]");
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }
            log.warn("[VALIDATION] Некорректное значение '{}' для поля '{}'",
                    invalidValue, fieldName);
            body.put("message", "Некорректное значение в поле " + fieldName + ": '" + invalidValue + "'");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        if (cause instanceof MismatchedInputException mismatchedInputException) {
            String field = mismatchedInputException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("неизвестное поле");

            body.put("message", "Некорректный тип данных в поле: " + field);
        } else {
            body.put("message", "Ошибка чтения тела запроса: " + ex.getMessage());
        }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
