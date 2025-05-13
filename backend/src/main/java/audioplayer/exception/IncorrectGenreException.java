package audioplayer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class IncorrectGenreException extends RuntimeException {
    public IncorrectGenreException(String message) {
        super(message);
    }
}
