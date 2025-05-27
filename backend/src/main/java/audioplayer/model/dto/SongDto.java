package audioplayer.model.dto;

import audioplayer.model.entity.Artist;
import audioplayer.model.entity.Playlist;
import audioplayer.model.entity.Song;
import audioplayer.utils.Genres;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private Long id;
    private String title;
    private List<String> artists;
    private List<String> playlist;
    private Genres genre;
    private String filePath;

    public SongDto(Song song) {
        this.title = song.getTitle();
        List<Artist> artistSet = song.getArtist();
        if (artistSet != null) {
            this.artists = artistSet.stream().map(Artist::getName).toList();
        }
        if (song.getPlaylists() != null) {
            this.playlist = song.getPlaylists().stream().map(Playlist::getName).toList();
        }
        this.genre = song.getGenre();
        this.filePath = song.getFilePath();
        this.id = song.getId();
    }
}
