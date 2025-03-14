package by.lapil.audioplayer.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAlbumDto {
    String name;
    Long artist;
    List<Long> songs;
}
