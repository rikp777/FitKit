package nl.rikp.paymentservice.handler;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.paymentservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final String ERROR_CODE_PREFIX = "ERR::";

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.error("Controller advise: Illegal state: {}", e.getMessage());

        var errorStatus = HttpStatus.BAD_REQUEST;
        var messageTitle = errorStatus.getReasonPhrase();
        var messageDescription = e.getMessage();
        var errorCode = ERROR_CODE_PREFIX + errorStatus.value();

        var errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(messageTitle + ":: " + messageDescription)
                .build();

        return ResponseEntity
                .status(errorStatus)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Controller advise: Validation error: {}", e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        var errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        var messageTitle = errorStatus.getReasonPhrase();
        var messageDescription = "An unexpected error occurred. Please try again later";
        var errorCode = ERROR_CODE_PREFIX + errorStatus.value();

        var errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(messageTitle + ":: " + messageDescription)
                .validationErrors(errors)
                .build();

        return ResponseEntity
                .status(errorStatus)
                .body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Controller advise: Unexpected error: {}", e.getMessage());

        var errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        var messageTitle = errorStatus.getReasonPhrase();
        var messageDescription = "An unexpected error occurred. Please try again later";
        var errorCode = ERROR_CODE_PREFIX + HttpStatus.INTERNAL_SERVER_ERROR.value();

        var errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(messageTitle + ":: " + messageDescription)
                .build();

        return ResponseEntity
                .status(errorStatus)
                .body(errorResponse);
    }
}
