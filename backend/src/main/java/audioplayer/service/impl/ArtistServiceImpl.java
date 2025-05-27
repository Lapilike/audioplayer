package audioplayer.service.impl;

import audioplayer.exception.NotFoundException;
import audioplayer.model.dto.ArtistDto;
import audioplayer.model.dto.CreateArtistDto;
import audioplayer.model.dto.UpdateArtistDto;
import audioplayer.model.entity.Artist;
import audioplayer.model.entity.Song;
import audioplayer.repository.ArtistRepository;
import audioplayer.service.ArtistService;
import audioplayer.service.SongService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final SongService songService;

    public ArtistServiceImpl(ArtistRepository artistRepository,
                             SongService songService) {
        this.artistRepository = artistRepository;
        this.songService = songService;
    }

    @Override
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    @Override
    public Artist findById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ARTIST_NOT_FOUND));
    }

    @Override
    public List<Artist> findAllById(List<Long> ids) {
        return artistRepository.findAllById(ids);
    }

    @Override
    public List<ArtistDto> findByName(String name) {
        List<Artist> artistList = artistRepository.findByName(name);
        if (artistList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ARTIST_NOT_FOUND);
        }
        return artistList.stream().map(ArtistDto::new).toList();
    }

    @Override
    public List<ArtistDto> findAllByName(List<String> names) {
        List<Artist> artistList = artistRepository.findAllByNameInIgnoreCase(names);
        if (artistList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ARTIST_NOT_FOUND);
        }
        return artistList.stream().map(ArtistDto::new).toList();
    }

    @Override
    public ArtistDto update(Long id, UpdateArtistDto updateDto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ARTIST_NOT_FOUND));
        artist.setName(updateDto.getName());
        List<Song> songList = songService.findAllById(updateDto.getSongIds());
        artist.setSongs(songList);
        songList.forEach(song -> song.getArtist().add(artist));
        return new ArtistDto(artistRepository.save(artist));
    }

    @Override
    public List<ArtistDto> update(List<Artist> artistList) {
        if (artistList == null || artistList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ARTIST_NOT_FOUND);
        }
        List<Artist> artistSaveList = artistRepository.saveAll(artistList);
        return artistSaveList.stream().map(ArtistDto::new).toList();
    }

    @Override
    public ArtistDto patch(Long id, UpdateArtistDto updateDto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ARTIST_NOT_FOUND));
        if (updateDto.getName() != null) {
            artist.setName(updateDto.getName());
        }
        if (updateDto.getSongIds() != null) {
            List<Song> songList = songService.findAllById(updateDto.getSongIds());
            artist.setSongs(songList);
            songList.forEach(song -> song.getArtist().add(artist));
        }
        return new ArtistDto(artistRepository.save(artist));
    }

    @Override
    public ArtistDto create(CreateArtistDto createArtistDto) {
        Artist artist = new Artist(createArtistDto);
        Artist savedArtist = artistRepository.save(artist);
        return new ArtistDto(savedArtist);
    }

    @Override
    public List<ArtistDto> createBulk(List<CreateArtistDto> createArtistDto) {
        List<String> names = createArtistDto.stream().map(CreateArtistDto::getName).toList();

        List<Artist> existingArtists = artistRepository.findAllByNameInIgnoreCase(names);

        List<String> existingNames = existingArtists.stream().map(Artist::getName).toList();

        List<Artist> artistList = createArtistDto.stream()
                .filter(artist -> !existingNames.contains(artist.getName()))
                .map(Artist::new).toList();

        List<Artist> createdArtists = artistRepository.saveAll(artistList);
        return createdArtists.stream().map(ArtistDto::new).toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ARTIST_NOT_FOUND));

        List<Long> songIdsToDelete = artist.getSongs().stream()
                .filter(song -> song.getArtist().size() == 1 && song.getArtist().contains(artist))
                .map(Song::getId)
                .toList();

        songIdsToDelete.forEach(songService::deleteById);

        artist.getSongs().clear();

        artistRepository.delete(artist);
    }
}
