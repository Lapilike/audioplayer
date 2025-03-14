package by.lapil.audioplayer.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSongDto {
    private String title;
    private List<Long> artists;
    private String genre;
    private String filePath;
}
