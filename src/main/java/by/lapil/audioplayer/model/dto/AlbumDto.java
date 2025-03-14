package by.lapil.audioplayer.model.dto;

import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Song;
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

    public AlbumDto(Album album) {
        this.name = album.getName();
        this.artistName = album.getArtist().getName();
        this.songName = album.getSongs().stream().map(Song::getTitle).toList();
    }
}
