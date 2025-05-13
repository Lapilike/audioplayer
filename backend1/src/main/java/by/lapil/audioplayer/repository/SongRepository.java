package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.entity.Song;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query(value = "SELECT DISTINCT s.* FROM song s" +
                 "    JOIN artist_song_id sa ON s.id = sa.song_id" +
                 "    JOIN artist a ON sa.artist_id = a.id" +
                 "    WHERE (LOWER(a.name) LIKE LOWER(CONCAT('%', :artistName, '%')))" +
                 "    AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')))",
                 nativeQuery = true)
      List<Song> findByCriteria(@Param("artistName") String artistName, @Param("title") String title);
}
