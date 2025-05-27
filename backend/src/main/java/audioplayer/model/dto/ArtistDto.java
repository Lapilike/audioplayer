package audioplayer.model.dto;

import audioplayer.model.entity.Artist;
import audioplayer.model.entity.Song;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {
    private String name;
    private List<String> songs;
    private Long id;

    public ArtistDto(Artist artist) {
        name = artist.getName();
        List<Song> songList = artist.getSongs();
        if (songList != null) {
            songs = songList.stream().map(Song::getTitle).toList();
        }
        this.id = artist.getId();
    }
}
