package audioplayer.service;

import audioplayer.model.dto.ArtistDto;
import audioplayer.model.dto.CreateArtistDto;
import audioplayer.model.dto.UpdateArtistDto;
import audioplayer.model.entity.Artist;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ArtistService {
    List<Artist> findAll();

    Artist findById(Long id);

    List<Artist> findAllById(List<Long> ids);

    List<ArtistDto> findByName(String name);

    List<ArtistDto> findAllByName(List<String> names);

    ArtistDto update(Long id, UpdateArtistDto updateDto);

    List<ArtistDto> update(List<Artist> artistList);

    ArtistDto patch(Long id, UpdateArtistDto updateDto);

    ArtistDto create(CreateArtistDto createArtistDto);

    List<ArtistDto> createBulk(List<CreateArtistDto> createArtistDto);

    void deleteById(Long id);
}
