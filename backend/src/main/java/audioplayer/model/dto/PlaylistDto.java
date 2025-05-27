package audioplayer.model.dto;

import audioplayer.model.entity.Playlist;
import audioplayer.model.entity.Song;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistDto {
    private String name;
    private List<Long> songIds;
    private Long id;

    public PlaylistDto(Playlist playlist) {
        this.name = playlist.getName();
        this.songIds = playlist.getSongs().stream().map(Song::getId).toList();
        this.id = playlist.getId();
    }
}
