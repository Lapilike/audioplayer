package by.lapil.audioplayer.model.dto;

import by.lapil.audioplayer.model.entity.Album;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDto {
    private String name;
    private String artistName;
    private Long artistId;

    public AlbumDto(Album album) {
        this.name = album.getName();
        this.artistName = album.getArtist().getName();
        this.artistId = album.getArtist().getId();
    }
}
