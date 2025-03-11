package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.Music;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
    Music findByGenre(String genre);

    void deleteById(Long id);
}
