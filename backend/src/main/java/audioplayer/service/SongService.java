package audioplayer.service;

import audioplayer.model.dto.CreateSongDto;
import audioplayer.model.dto.SongDto;
import audioplayer.model.entity.Song;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface SongService {
    SongDto create(CreateSongDto createSongDto);

    List<Song> findAll();

    List<Song> findAllById(List<Long> ids);

    Song findById(Long id);

    List<SongDto> findByCriteria(String title, String artistName);

    SongDto update(Long id, CreateSongDto createSongDto);

    List<SongDto> update(List<Song> songs);

    SongDto patch(Long id, CreateSongDto createSongDto);

    void deleteById(Long id);

    void parseAll();

    void changeDir();
}
