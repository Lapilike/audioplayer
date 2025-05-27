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
public class CreatePlaylistDto {
    @NotBlank(message = "Album name must be provided", groups = {UpdateArtistDto.Groups.OnUpdate.class})
    @NotNull(message = "Album name must be provided", groups = {UpdateArtistDto.Groups.OnUpdate.class})
    String name;

    List<Long> songs;

    public static class Groups {
        public interface OnPatch {}
    }
}
