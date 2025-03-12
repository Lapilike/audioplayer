package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.dto.UpdateArtistDto;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.ArtistRepository;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.NotFoundExeption;
import by.lapil.audioplayer.service.SongService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final SongService songService;

    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public Artist findById(Long id) {
        return artistRepository.findById(id).orElse(null);
    }

    @Override
    public ArtistDto update(Long id, UpdateArtistDto updateDto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Artist not found"));
        if (updateDto.getArtistSongs() != null) {
            List<Song> songList = songService.findAllById(updateDto.getArtistSongs().stream().toList());
            artist.setSongs(new HashSet<>(songList));
        }
        return new ArtistDto(artistRepository.save(artist));
    }

    @Override
    public ArtistDto create(CreateArtistDto createArtistDto) {
        Artist artist = new Artist(createArtistDto);
        artistRepository.save(artist);
        return new ArtistDto(artist);
    }

    @Override
    public void deleteById(Long id) {
        artistRepository.deleteById(id);
    }

    @Override
    public Set<Artist> findByName(Set<String> names) {
        return artistRepository.findByName(names);
    }
}
