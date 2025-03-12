package by.lapil.audioplayer.service;

import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.dto.UpdateArtistDto;
import by.lapil.audioplayer.model.entity.Artist;
import java.util.List;
import java.util.Set;

public interface ArtistService {
    List<Artist> findAll();

    Artist findById(Long id);

    Set<Artist> findByName(Set<String> names);

    ArtistDto update(Long id, UpdateArtistDto updateDto);

    ArtistDto create(CreateArtistDto createArtistDto);

    void deleteById(Long id);
}
