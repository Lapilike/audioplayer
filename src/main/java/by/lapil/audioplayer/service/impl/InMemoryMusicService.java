package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.Music;
import by.lapil.audioplayer.repository.InMemoryMusicDao;
import by.lapil.audioplayer.service.MusicService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InMemoryMusicService implements MusicService {
    InMemoryMusicDao musicDao;

    @Override
    public List<Music> findAll() {
        return musicDao.findAll();
    }

    @Override
    public Music findById(int id) {
        return musicDao.findById(id);
    }

    @Override
    public Music update(Music music) {
        return musicDao.update(music);
    }

    @Override
    public Music save(Music music) {
        return musicDao.save(music);
    }

    @Override
    public void deleteById(int id) {
        musicDao.deleteById(id);
    }
}
