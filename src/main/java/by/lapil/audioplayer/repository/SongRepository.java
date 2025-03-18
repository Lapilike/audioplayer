package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.entity.Song;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query("SELECT s FROM Song s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :titlePart, '%'))")
    List<Song> findByTitle(@Param("titlePart") String title);

    @Query("SELECT DISTINCT s FROM Song s " +
            "JOIN s.artist a " +
            "WHERE (:title IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:genre IS NULL OR s.genre = :genre) " +
            "AND (:artistName IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :artistName, '%')))")
    List<Song> findByCriteria(@Param("title") String title,
                              @Param("genre") String genre,
                              @Param("artistName") String artistName);

    //  Query(value = "SELECT DISTINCT s.* FROM song s" +
    //             "    JOIN artist_song_id sa ON s.id = sa.song_id" +
    //             "    JOIN artist a ON sa.artist_id = a.id" +
    //             "    WHERE (:title IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')))" +
    //             "    AND (:genre IS NULL OR s.genre = :genre)" +
    //             "    AND (:artistName IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :artistName, '%')))",
    //             nativeQuery = true)
    //  List<Song> findByArtist(@Param("artistName") String artistName);
}
