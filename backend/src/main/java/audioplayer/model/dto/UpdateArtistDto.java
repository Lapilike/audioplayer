package audioplayer.model.dto;

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
public class UpdateArtistDto {
    @NotBlank(message = "Artist name must be provided")
    @NotEmpty(message = "Artist name must be provided", groups = {Groups.OnUpdate.class})
    String name;

    @NotNull(message = "Artist songs must be provided", groups = {Groups.OnUpdate.class})
    @NotEmpty(message = "Artist songs must be provided", groups = {Groups.OnUpdate.class})
    List<Long> songIds;

    public static class Groups {
        public interface OnPatch {}

        public interface OnUpdate {}
    }
}
