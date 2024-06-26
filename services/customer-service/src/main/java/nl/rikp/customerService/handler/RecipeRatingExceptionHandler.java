package nl.rikp.customerService.handler;

import lombok.extern.slf4j.Slf4j;
import nl.rikp.customerService.dto.ErrorResponse;
import nl.rikp.customerService.exception.RecipeRatingAlreadyExistsException;
import nl.rikp.customerService.exception.notFound.RecipeRatingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
public class RecipeRatingExceptionHandler {

    @ExceptionHandler(RecipeRatingAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRecipeRatingAlreadyExistsException(RecipeRatingAlreadyExistsException e) {
        log.error("Controller advise: Recipe rating already exists: {}", e.getMessage());

        var errorStatus = HttpStatus.BAD_REQUEST;
        var messageTitle = errorStatus.getReasonPhrase();
        var messageDescription = e.getMessage();
        var errorCode =  "ERR::" + errorStatus.value();

        var errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(messageTitle + ":: " + messageDescription)
                .build();

        return ResponseEntity
                .status(errorStatus)
                .body(errorResponse);
    }

    @ExceptionHandler(RecipeRatingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRecipeRatingNotFoundException(RecipeRatingNotFoundException e) {
        log.error("Controller advise: Recipe rating not found: {}", e.getMessage());

        var errorStatus = HttpStatus.NOT_FOUND;
        var messageTitle = errorStatus.getReasonPhrase();
        var messageDescription = e.getMessage();
        var errorCode =  "ERR::" + errorStatus.value();

        var errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(messageTitle + ":: " + messageDescription)
                .build();

        return ResponseEntity
                .status(errorStatus)
                .body(errorResponse);
    }
}
