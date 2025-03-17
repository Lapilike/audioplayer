package by.lapil.audioplayer.model.dto;

import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.utils.Genres;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private String title;
    private List<String> artists;
    private String album;
    private Genres genre;
    private String filePath;
    private Long id;

    public SongDto(Song song) {
        this.title = song.getTitle();
        List<Artist> artistSet = song.getArtist();
        if (artistSet != null) {
            this.artists = artistSet.stream().map(Artist::getName).toList();
        }
        if (song.getAlbum() != null) {
            this.album = song.getAlbum().getName();
        }
        this.genre = song.getGenre();
        this.filePath = song.getFilePath();
        this.id = song.getId();
    }
}
