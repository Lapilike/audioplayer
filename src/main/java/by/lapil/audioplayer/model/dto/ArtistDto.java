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
    private String artistName;
    private List<String> artistSongs;
    private List<String> artistAlbums;

    public ArtistDto(Artist artist) {
        artistName = artist.getName();
        List<Song> songList = artist.getSongs();
        if (songList != null) {
            artistSongs = songList.stream().map(Song::getTitle).toList();
        }
        List<Album> albumList = artist.getAlbums();
        if (albumList != null) {
            artistAlbums = albumList.stream().map(Album::getName).toList();
        }
    }
}
