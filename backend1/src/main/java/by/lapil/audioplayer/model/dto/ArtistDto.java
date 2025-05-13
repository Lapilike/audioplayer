package by.lapil.audioplayer.model.dto;

import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
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
    private List<String> albums;
    private Long id;

    public ArtistDto(Artist artist) {
        name = artist.getName();
        List<Song> songList = artist.getSongs();
        if (songList != null) {
            songs = songList.stream().map(Song::getTitle).toList();
        }
        List<Album> albumList = artist.getAlbums();
        if (albumList != null) {
            albums = albumList.stream().map(Album::getName).toList();
        }
        this.id = artist.getId();
    }
}
