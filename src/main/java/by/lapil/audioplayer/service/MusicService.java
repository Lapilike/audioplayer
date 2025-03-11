package by.lapil.audioplayer.service;

import by.lapil.audioplayer.model.Music;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface MusicService {
    List<Music> findAll();

    Music findById(Long id);

    Music update(Music music);

    Music save(Music music);

    void deleteById(Long id);
}
