package in.akhilesh.instantcart.exception;

import in.akhilesh.instantcart.dto.exception.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionhandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAuthorizationDeniedException(Exception e) {
        ExceptionResponseDTO exceptionResponse = ExceptionResponseDTO.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.FORBIDDEN)
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }
}
