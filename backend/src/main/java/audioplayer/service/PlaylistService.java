package audioplayer.service;

import audioplayer.model.dto.CreatePlaylistDto;
import audioplayer.model.dto.PlaylistDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface PlaylistService {
    List<PlaylistDto> findAll();

    List<PlaylistDto> findByName(String name);

    PlaylistDto findById(Long id);

    PlaylistDto create(CreatePlaylistDto createDto);

    PlaylistDto patch(Long id, CreatePlaylistDto createDto);

    void delete(Long id);
}