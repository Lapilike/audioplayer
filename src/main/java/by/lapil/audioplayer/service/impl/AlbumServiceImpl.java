package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.dto.AlbumDto;
import by.lapil.audioplayer.model.dto.CreateAlbumDto;
import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.AlbumRepository;
import by.lapil.audioplayer.service.AlbumService;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.SongService;
import by.lapil.audioplayer.utils.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    private final AlbumRepository albumRepository;
    private final SongService songService;
    private final ArtistService artistService;

    @Override
    public List<AlbumDto> findAll() {
        List<Album> albumList = albumRepository.findAll();
        return albumList.stream().map(AlbumDto::new).toList();
    }

    @Override
    public List<Album> findAllById(List<Long> ids) {
        return albumRepository.findAllById(ids);
    }

    @Override
    public List<AlbumDto> findByName(String name) {
        List<Album> albumList = albumRepository.findByName(name);
        if (albumList.isEmpty()) {
            throw new NotFoundException("Album not found");
        }
        return albumList.stream().map(AlbumDto::new).toList();
    }

    @Override
    public AlbumDto create(CreateAlbumDto createDto) {
        Album album = new Album(createDto);
        List<Song> songList = songService.findAllById(createDto.getSongs());
        Artist artist = artistService.findById(createDto.getArtist());
        album.setSongs(songList);
        album.setArtist(artist);
        final Album savedAlbum = albumRepository.save(album);

        artist.getAlbums().add(album);
        artistService.update(new ArrayList<>(List.of(artist)));

        songList.forEach(song -> song.setAlbum(album));
        songService.update(songList);

        return new AlbumDto(savedAlbum);
    }

    @Override
    public AlbumDto update(Long id, CreateAlbumDto createDto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Album not found"));
        if (!createDto.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields must be filled");
        }
        album.setName(createDto.getName());
        return new AlbumDto(albumRepository.save(album));
    }

    @Override
    public void delete(Long id) {
        albumRepository.deleteById(id);
    }
}
