package by.lapil.audioplayer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public static final String SONG_NOT_FOUND = "Song not found";
    public static final String ALBUM_NOT_FOUND = "Album not found";
    public static final String ARTIST_NOT_FOUND = "Artist not found";

    public NotFoundException(String message) {
        super(message);
    }
}
