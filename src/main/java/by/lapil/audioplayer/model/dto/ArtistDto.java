package by.lapil.audioplayer.model.dto;

import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {
    private String artistName;
    private Set<String> artistSongs;
    private Set<String> artistAlbums;

    public ArtistDto(Artist artist) {
        artistName = artist.getArtistName();
        Set<Song> songSet = artist.getSongs();
        artistSongs = songSet.stream().map(Song::getTitle).collect(Collectors.toSet());
        Set<Album> albumSet = artist.getAlbums();
        artistAlbums = albumSet.stream().map(Album::getName).collect(Collectors.toSet());
    }
}
