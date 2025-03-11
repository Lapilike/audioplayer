package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.Music;
import by.lapil.audioplayer.repository.MusicRepository;
import by.lapil.audioplayer.service.MusicService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MusicServiceImpl implements MusicService {
    private final MusicRepository musicRepository;

    @Override
    public List<Music> findAll() {
        return musicRepository.findAll();
    }

    @Override
    public Music findById(Long id) {
        return null;
    }

    @Override
    public Music update(Music music) {
        return musicRepository.save(music);
    }

    @Override
    public Music save(Music music) {
        return musicRepository.save(music);
    }

    @Override
    public void deleteById(Long id) {
        musicRepository.deleteById(id);
    }
}
