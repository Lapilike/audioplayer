package by.lapil.audioplayer.service;

import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.dto.UpdateArtistDto;
import by.lapil.audioplayer.model.entity.Artist;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ArtistService {
    List<Artist> findAll();

    Artist findById(Long id);

    List<Artist> findAllById(List<Long> ids);

    List<ArtistDto> findByName(String name);

    ArtistDto update(Long id, UpdateArtistDto updateDto);

    List<ArtistDto> update(List<Artist> artistList);

    ArtistDto patch(Long id, UpdateArtistDto updateDto);

    ArtistDto create(CreateArtistDto createArtistDto);

    List<ArtistDto> createBulk(List<CreateArtistDto> createArtistDto);

    void deleteById(Long id);
}
