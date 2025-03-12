package by.lapil.audioplayer.model.dto;

import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.utils.Genres;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private String title;
    private Set<String> artists;
    private Genres genre;
    private String filePath;

    public SongDto(Song song) {
        title = song.getTitle();
        Set<Artist> artistSet = song.getArtist();
        artists = artistSet.stream().map(Artist::getArtistName).collect(Collectors.toSet());
        genre = song.getGenre();
        filePath = song.getFilePath();
    }
}
