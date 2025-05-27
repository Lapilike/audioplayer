package audioplayer.model.dto;

import audioplayer.utils.Genres;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSongDto {
    @NotBlank(message = "title must be provided")
    private String title;

    @NotEmpty(message = "artists must be provided")
    private List<Long> artists;

    @NotNull(message = "genre must be provided")
    private Genres genre;

    @NotBlank(message = "FilePath must be provided")
    private String filePath;

    private Long songId;
}
