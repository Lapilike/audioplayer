package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.dto.ArtistDto;
import by.lapil.audioplayer.model.dto.CreateArtistDto;
import by.lapil.audioplayer.model.dto.UpdateArtistDto;
import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.ArtistRepository;
import by.lapil.audioplayer.service.AlbumService;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.SongService;
import by.lapil.audioplayer.utils.NotFoundException;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final AlbumService albumService;
    private final SongService songService;

    public ArtistServiceImpl(ArtistRepository artistRepository,
                             @Lazy AlbumService albumService,
                             SongService songService) {
        this.artistRepository = artistRepository;
        this.albumService = albumService;
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
        return artistList.stream().map(ArtistDto::new).toList();
    }

    @Override
    public ArtistDto update(Long id, UpdateArtistDto updateDto) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ARTIST_NOT_FOUND));
        if (updateDto.getArtistName() == null ||
                updateDto.getAlbumsIds() == null ||
                updateDto.getSongIds() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields must be filled");
        }
        artist.setName(updateDto.getArtistName());
        List<Album> albumList = albumService.findAllById(updateDto.getAlbumsIds());
        artist.setAlbums(albumList);
        albumList.forEach(album -> album.setArtist(artist));
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
        if (updateDto.getArtistName() != null) {
            artist.setName(updateDto.getArtistName());
        }
        if (updateDto.getAlbumsIds() != null) {
            List<Album> albumList = albumService.findAllById(updateDto.getAlbumsIds());
            artist.setAlbums(albumList);
            albumList.forEach(album -> album.setArtist(artist));
        }
        if (updateDto.getSongIds() != null) {
            List<Album> albumList = albumService.findAllById(updateDto.getAlbumsIds());
            artist.setAlbums(albumList);
            albumList.forEach(album -> album.setArtist(artist));
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
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ARTIST_NOT_FOUND));

        List<Song> songs = artist.getSongs();
        songs.forEach(song -> song.getArtist().remove(artist));
        artist.getSongs().clear();
        for (Song song : songs) {
            if (song.getArtist().isEmpty()) {
                songService.deleteById(song.getId());
            }
        }
        artistRepository.delete(artist);
    }
}
