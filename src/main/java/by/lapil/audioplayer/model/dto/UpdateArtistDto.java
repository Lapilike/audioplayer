package by.lapil.audioplayer.model.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArtistDto {
    Set<Long> artistSongs;
    Set<Long> artistAlbums;
}
