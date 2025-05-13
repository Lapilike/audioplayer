package audioplayer.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAlbumDto {
    @NotBlank(message = "Album name must be provided")
    @NotNull(message = "Album name must be provided")
    String name;

    @NotNull(message = "Artist must be provided")
    Long artist;

    List<Long> songs;
}
