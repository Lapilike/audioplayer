package by.lapil.audioplayer.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public static final String SONG_NOT_FOUND = "Song Not Found";
    public static final String ALBUM_NOT_FOUND = "Album Not Found";
    public static final String ARTIST_NOT_FOUND = "Artist Not Found";

    public NotFoundException(String message) {
        super(message);
    }
}
