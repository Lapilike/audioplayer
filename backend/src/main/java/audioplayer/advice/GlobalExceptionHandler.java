package audioplayer.advice;

import audioplayer.exception.NotFoundException;
import audioplayer.exception.ValidationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String PATH = "path";

    private Map<String, Object> createBadRequestResponse(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put(ERROR, "Ошибка чтения тела запроса");
        body.put(MESSAGE, message);

        return body;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(field ->
                errors.put(field.getField(), field.getDefaultMessage())
        );
        log.error("Validation errors: {}", errors);
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleCustomValidation(ValidationException ex) {
        return new ResponseEntity<>(createBadRequestResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, Object> body = createBadRequestResponse(ex.getMessage());

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            Class<?> targetType = ife.getTargetType();

            String fieldName = ife.getPath().get(0).getFieldName();
            String invalidValue = String.valueOf(ife.getValue());

            if (targetType.isEnum()) {
                Object[] allowed = targetType.getEnumConstants();

                String allowedValues = String.join(", ",
                        java.util.Arrays.stream(allowed).map(Object::toString).toList());

                log.error("[VALIDATION] Некорректное значение '{}' для поля '{}'. Разрешено: {}",
                        invalidValue, fieldName, allowedValues);

                body.put(MESSAGE, "Некорректное значение в поле " + fieldName + ": '" + invalidValue +
                        "'. Разрешённые значения: [" + allowedValues + "]");
                return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
            }
            log.error("[VALIDATION] Некорректное значение '{}' для поля '{}'",
                    invalidValue, fieldName);
            body.put(MESSAGE, "Некорректное значение в поле " + fieldName + ": '" + invalidValue + "'");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        if (cause instanceof MismatchedInputException mismatchedInputException) {
            String field = mismatchedInputException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse("неизвестное поле");

            body.put(MESSAGE, "Некорректный тип данных в поле: " + field);
        } else {
            body.put(MESSAGE, "Ошибка чтения тела запроса: " + ex.getMessage());
        }

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex,
                                                                    HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, ex.getStatusCode().value());
        body.put(ERROR, ex.getStatusCode());
        body.put(MESSAGE, ex.getReason());
        body.put(PATH, request.getRequestURI());

        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex,
                                                              HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.NOT_FOUND.value());
        body.put(ERROR, HttpStatus.NOT_FOUND.value());
        body.put(MESSAGE, ex.getMessage());
        body.put(PATH, request.getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
