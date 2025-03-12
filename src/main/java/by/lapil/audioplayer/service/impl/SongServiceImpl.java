package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.SongRepository;
import by.lapil.audioplayer.service.ArtistService;
import by.lapil.audioplayer.service.NotFoundExeption;
import by.lapil.audioplayer.service.SongService;
import by.lapil.audioplayer.utils.Genres;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Primary
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final ArtistService artistService;

    @Override
    public SongDto create(CreateSongDto createSongDto) {
        Song song = new Song(createSongDto);
        Set<Artist> artistSet = artistService.findByName(createSongDto.getArtists());
        song.setArtist(artistSet);
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
        return null;
    }

    @Override
    public List<SongDto> findByTitleAndGenre(String title, Genres genre) {
        List<Song> songs = songRepository.findByTitle(title);
        songRepository.findByTitle(title);
        List<SongDto> songDtos = songs.stream().map(m -> new SongDto(m)).collect(Collectors.toList());
        if (genre != null) {
            songDtos = songDtos.stream()
                    .filter(u -> u.getGenre().equals(genre))
                    .collect(Collectors.toList());
        }
        return songDtos;
    }

    @Override
    public Set<Song> findByTitles(Set<String> titles) {
        return songRepository.findByTitles(titles);
    }

    @Override
    public SongDto update(Long id, CreateSongDto createSongDto) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new NotFoundExeption("Song not found"));
        if (createSongDto.getTitle() != null) {
            song.setTitle(createSongDto.getTitle());
        }
        if (createSongDto.getArtists() != null) {
            song.setArtist(artistService.findByName(createSongDto.getArtists()));
        }
        if (createSongDto.getGenre() != null) {
            try {
                song.setGenre(Genres.parseGenre(createSongDto.getGenre().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Некорректный жанр: " + createSongDto.getGenre());
            }
        }
        return new SongDto(song);
    }

    @Override
    public Song save(Song song) {
        return songRepository.save(song);
    }

    @Override
    public void deleteById(Long id) {
        songRepository.deleteById(id);
    }
}
