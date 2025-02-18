package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.Music;
import by.lapil.audioplayer.repository.InMemoryMusicDao;
import by.lapil.audioplayer.service.MusicService;
import by.lapil.audioplayer.service.NotFoundExeption;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InMemoryMusicService implements MusicService {
    InMemoryMusicDao musicDao;

    @Override
    public List<Music> findAll() {
        List<Music> music = musicDao.findAll();
        if (music.isEmpty()) {
            throw new NotFoundExeption("No music found");
        }

        return music;
    }

    @Override
    public Music findById(int id) {
        Music music = musicDao.findById(id);
        if (music == null) {
            throw new NotFoundExeption("Music not found");
        }
        return music;
    }

    @Override
    public Music update(Music music) {
        Music tmpMusic = musicDao.update(music);
        if (tmpMusic == null) {
            throw new NotFoundExeption("Music not found");
        }
        return tmpMusic;
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
