package audioplayer.model.dto;

import audioplayer.model.entity.Album;
import audioplayer.model.entity.Song;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDto {
    private String name;
    private String artistName;
    private List<String> songName;
    private Long id;

    public AlbumDto(Album album) {
        this.name = album.getName();
        this.artistName = album.getArtist().getName();
        this.songName = album.getSongs().stream().map(Song::getTitle).toList();
        this.id = album.getId();
    }
}
