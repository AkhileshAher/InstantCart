package in.akhilesh.instantcart.exception;

import in.akhilesh.instantcart.dto.exception.ExceptionResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleProductFoundException(Exception e) {
        ExceptionResponseDTO exceptionResponse = ExceptionResponseDTO.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.NOT_FOUND)
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponseDTO> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        ExceptionResponseDTO exceptionResponse = ExceptionResponseDTO.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleException(Exception e, HttpServletRequest request) {
        ExceptionResponseDTO exceptionResponse = ExceptionResponseDTO.builder()
                .message(e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

}
