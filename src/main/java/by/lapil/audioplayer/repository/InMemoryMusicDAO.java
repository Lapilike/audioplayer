package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.Music;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemoryMusicDAO {
    private final List<Music> musics = new ArrayList<>();

    public List<Music> findAll() {
        return musics;
    }

    public Music findById(int id) {
        return musics.stream()
                .filter(music -> music.getId() == id)
                .findFirst().orElse(null);
    }

    public Music update(Music music) {
        int MusicIndex = IntStream.range(0, musics.size())
                .filter(i -> musics.get(i).getId() == music.getId())
                .findFirst().orElse(-1);
        if(MusicIndex != -1) {
            musics.set(MusicIndex, music);
            return music;
        }
        return null;
    }

    public Music save(Music music) {
        musics.add(music);
        return music;
    }

    public void deleteById(int id) {
        musics.remove(findById(id));
    }
}
