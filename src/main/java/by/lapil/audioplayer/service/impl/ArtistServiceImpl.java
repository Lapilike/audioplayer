package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.exception.NotFoundException;
import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.dto.UpdateArtistDto;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.ArtistRepository;
import by.lapil.audioplayer.service.AlbumService;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.SongService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final SongService songService;
    private final AlbumService albumService;

    public ArtistServiceImpl(ArtistRepository artistRepository,
                             SongService songService,
                             @Lazy AlbumService albumService) {
        this.artistRepository = artistRepository;
        this.songService = songService;
        this.albumService = albumService;
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

    @Transactional
    @Override
    public void deleteById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ARTIST_NOT_FOUND));

        artist.getAlbums().forEach(album -> albumService.delete(album.getId()));
        artist.getAlbums().clear();

        List<Long> songIdsToDelete = artist.getSongs().stream()
                .filter(song -> song.getArtist().size() == 1 && song.getArtist().contains(artist))
                .map(Song::getId)
                .toList();

        songIdsToDelete.forEach(songService::deleteById);

        artist.getSongs().clear();

        artistRepository.delete(artist);
    }
}
