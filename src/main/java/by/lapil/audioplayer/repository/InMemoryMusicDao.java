package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.Music;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMusicDao {
    private final List<Music> musics = new ArrayList<>();

    public List<Music> findAll() {
        return musics;
    }

    public Music findById(Long id) {
        return musics.stream()
                .filter(music -> music.getId() == id)
                .findFirst().orElse(null);
    }

    public Music update(Music music) {
        int index = IntStream.range(0, musics.size())
                .filter(i -> musics.get(i).getId() == music.getId())
                .findFirst().orElse(-1);
        if (index != -1) {
            musics.set(index, music);
            return music;
        }
        return null;
    }

    public Music save(Music music) {
        musics.add(music);
        return music;
    }

    public void deleteById(Long id) {
        musics.remove(findById(id));
    }
}
