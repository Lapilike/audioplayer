package by.lapil.audioplayer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Music {
    private String name;
    private String artist;
    private String album;
    private String genre;
    private String path;
    private int id;
}
