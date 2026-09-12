package in.akhilesh.instantcart.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDTO {
    private String message;
    private HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    private String error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    private String path = "";
    private LocalDateTime timestamp;

}
