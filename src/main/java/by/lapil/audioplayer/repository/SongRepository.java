package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.entity.Song;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :titlePart, '%'))")
    List<Song> findByTitle(@Param("titlePart") String title);

    @Query("SELECT s FROM Song s WHERE s.title IN :titles")
    Set<Song> findByTitles(@Param("titles") Set<String> titles);

    void deleteById(Long id);
}
