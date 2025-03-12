package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.entity.Artist;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    @Query("SELECT a FROM Artist a WHERE a.artistName IN :names")
    Set<Artist> findByName(@Param("names") Set<String> names);
}
