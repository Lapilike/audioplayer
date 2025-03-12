package by.lapil.audioplayer.model.dto;

import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateSongDto {
    private String title;
    private Set<String> artists;
    private String genre;
    private String filePath;
}
