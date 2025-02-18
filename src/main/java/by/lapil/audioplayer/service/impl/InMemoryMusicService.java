package by.lapil.audioplayer.service.impl;

import by.lapil.audioplayer.model.Music;
import by.lapil.audioplayer.repository.InMemoryMusicDAO;
import by.lapil.audioplayer.service.MusicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryMusicService implements MusicService {

    InMemoryMusicDAO MUSIC_DAO;

    @Override
    public List<Music> findAll() {
        return MUSIC_DAO.findAll();
    }

    @Override
    public Music findById(int id) {
        return MUSIC_DAO.findById(id);
    }

    @Override
    public Music update(Music music) {
        return MUSIC_DAO.update(music);
    }

    @Override
    public Music save(Music music) {
        return MUSIC_DAO.save(music);
    }

    @Override
    public void deleteById(int id) {
        MUSIC_DAO.deleteById(id);
    }
}
