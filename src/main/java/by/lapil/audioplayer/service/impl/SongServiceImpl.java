package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Album;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.SongRepository;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.SongService;
import by.lapil.audioplayer.utils.Genres;
import by.lapil.audioplayer.utils.IncorrectGenreException;
import by.lapil.audioplayer.utils.NotFoundException;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Primary
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final ArtistService artistService;

    public SongServiceImpl(SongRepository songRepository, @Lazy ArtistService artistService) {
        this.songRepository = songRepository;
        this.artistService = artistService;
    }

    @Override
    public SongDto create(CreateSongDto createSongDto) {
        Song song = new Song();
        song.setTitle(createSongDto.getTitle());
        song.setGenre(Genres.parseGenre(createSongDto.getGenre()));
        song.setFilePath(createSongDto.getFilePath());

        List<Artist> artistList = artistService.findAllById(createSongDto.getArtists());
        song.setArtist(artistList);
        artistList.forEach(artist -> artist.getSongs().add(song));
        songRepository.save(song);
        return new SongDto(song);
    }

    @Override
    public List<Song> findAll() {
        return songRepository.findAll();
    }

    @Override
    public List<Song> findAllById(List<Long> ids) {
        return songRepository.findAllById(ids);
    }

    @Override
    public Song findById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.SONG_NOT_FOUND));
    }

    @Override
    public List<SongDto> findByTitleAndGenre(String title, String genre) {
        List<Song> songs;
        if (title != null && !title.isBlank()) {
            songs = songRepository.findByTitle(title);
        } else {
            songs = songRepository.findAll();
        }

        if (songs.isEmpty()) {
            throw new NotFoundException(NotFoundException.SONG_NOT_FOUND);
        }
        List<SongDto> songDtos = songs.stream()
                .map(SongDto::new)
                .toList();

        if (genre != null) {
            Genres genreEnum = Genres.parseGenre(genre);
            songDtos = songDtos.stream()
                    .filter(u -> u.getGenre().equals(genreEnum))
                    .toList();
        }
        return songDtos;
    }

    @Override
    public SongDto update(Long id, CreateSongDto createSongDto) {
        if (createSongDto.getTitle() == null ||
                createSongDto.getArtists() == null ||
                createSongDto.getGenre() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields must be filled");
        }

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.SONG_NOT_FOUND));
        song.setTitle(createSongDto.getTitle());
        addSongToArtist(createSongDto, song);

        try {
            song.setGenre(Genres.parseGenre(createSongDto.getGenre().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IncorrectGenreException("Incorrect genre: " + createSongDto.getGenre());
        }
        return new SongDto(songRepository.save(song));
    }

    @Override
    public List<SongDto> update(List<Song> songs) {
        if (songs == null) {
            throw new NotFoundException(NotFoundException.SONG_NOT_FOUND);
        }

        List<Song> savedSongs = songRepository.saveAll(songs);

        return savedSongs.stream().map(SongDto::new).toList();
    }

    private void addSongToArtist(CreateSongDto createDto, Song song) {
        List<Artist> artistList = artistService.findAllById(createDto.getArtists());

        if (!artistList.isEmpty()) {
            List<Artist> newArtistList = artistService.findAllById(createDto.getArtists());
            newArtistList.removeAll(song.getArtist());
            if (!newArtistList.isEmpty()) {
                song.getArtist().addAll(newArtistList);
                newArtistList.forEach(artist -> artist.getSongs().add(song));
                artistService.update(newArtistList);
            }

            List<Artist> removeArtistList = song.getArtist();
            removeArtistList.removeAll(artistList);
            if (!removeArtistList.isEmpty()) {
                removeArtistList.forEach(artist -> artist.getSongs().remove(song));
                artistService.update(removeArtistList);

                song.getArtist().removeAll(removeArtistList);
            }
        }
    }

    @Override
    public SongDto patch(Long id, CreateSongDto createSongDto) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.SONG_NOT_FOUND));
        if (createSongDto.getTitle() != null) {
            song.setTitle(createSongDto.getTitle());
        }
        if (createSongDto.getArtists() != null) {
            addSongToArtist(createSongDto, song);
        }
        if (createSongDto.getGenre() != null) {
            try {
                song.setGenre(Genres.parseGenre(createSongDto.getGenre().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IncorrectGenreException("Incorrect genre: " + createSongDto.getGenre());
            }
        }

        return new SongDto(songRepository.save(song));
    }

    @Override
    public void deleteById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NotFoundException.SONG_NOT_FOUND));
        List<Artist> artistList = song.getArtist();
        if (artistList != null && !artistList.isEmpty()) {
            artistList.forEach(artist -> artist.getSongs().remove(song));
        }

        Album album = song.getAlbum();
        if (album != null) {
            album.getSongs().remove(song);
        }

        songRepository.deleteById(song.getId());
    }
}
