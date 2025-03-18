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
import jakarta.transaction.Transactional;
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
    public List<AlbumDto> findByName(String name) {
        List<Album> albumList = albumRepository.findByName(name);
        if (albumList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ALBUM_NOT_FOUND);
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
    public AlbumDto patch(Long id, CreateAlbumDto createDto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ALBUM_NOT_FOUND));
        if (createDto.getName() != null && !createDto.getName().isBlank()) {
            album.setName(createDto.getName());
        }
        if (createDto.getArtist() != null) {
            try {
                Artist newArtist = artistService.findById(createDto.getArtist());
                newArtist.getAlbums().add(album);

                Artist oldArtist = album.getArtist();
                oldArtist.getAlbums().remove(album);

                album.setArtist(newArtist);

                artistService.update(List.of(oldArtist, newArtist));
                album.setArtist(newArtist);
            } catch (NotFoundException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }
        if (createDto.getSongs() != null) {
            List<Song> songList = songService.findAllById(createDto.getSongs());
            if (songList.isEmpty()) {
                List<Song> deletedSongList = album.getSongs();
                deletedSongList.forEach(song -> song.setAlbum(null));
                album.getSongs().clear();
            } else {
                List<Song> newSongList = songService.findAllById(createDto.getSongs());
                newSongList.removeAll(album.getSongs());
                newSongList.forEach(song -> song.setAlbum(album));

                List<Song> deletedSongList = album.getSongs();
                deletedSongList.removeAll(songList);
                deletedSongList.forEach(song -> song.setAlbum(null));

                album.getSongs().removeAll(deletedSongList);
                album.getSongs().addAll(newSongList);
            }
        }

        return new AlbumDto(albumRepository.save(album));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.ALBUM_NOT_FOUND));

        List<Song> songList = album.getSongs();
        if (songList != null && !songList.isEmpty()) {
            songList.forEach(song -> song.setAlbum(null));
            album.getSongs().clear();
            albumRepository.save(album);
        }

        albumRepository.deleteById(id);
    }
}
