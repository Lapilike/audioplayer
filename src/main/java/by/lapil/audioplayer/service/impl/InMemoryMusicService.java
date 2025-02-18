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

    InMemoryMusicDAO MusicRepository;

    @Override
    public List<Music> findAll() {
        return MusicRepository.findAll();
    }

    @Override
    public Music findById(int id) {
        return MusicRepository.findById(id);
    }

    @Override
    public Music update(Music music) {
        return MusicRepository.update(music);
    }

    @Override
    public Music save(Music music) {
        return MusicRepository.save(music);
    }

    @Override
    public void deleteById(int id) {
        MusicRepository.deleteById(id);
    }
}
