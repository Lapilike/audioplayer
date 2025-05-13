package audioplayer.service;

import audioplayer.model.dto.AlbumDto;
import audioplayer.model.dto.CreateAlbumDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AlbumService {
    List<AlbumDto> findAll();

    List<AlbumDto> findByName(String name);

    AlbumDto create(CreateAlbumDto createDto);

    AlbumDto patch(Long id, CreateAlbumDto createDto);

    void delete(Long id);
}