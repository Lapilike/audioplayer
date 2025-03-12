package by.lapil.audioplayer.service;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.utils.Genres;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface SongService {
    SongDto create(CreateSongDto createSongDto);

    List<Song> findAll();

    List<Song> findAllById(List<Long> ids);

    Song findById(Long id);

    List<SongDto> findByTitleAndGenre(String name, Genres genre);

    SongDto update(Long id, CreateSongDto createSongDto);

    void deleteById(Long id);
}
