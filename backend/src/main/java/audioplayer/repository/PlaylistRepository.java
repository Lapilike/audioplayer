package audioplayer.repository;

import audioplayer.model.entity.Playlist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("SELECT a FROM Playlist a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Playlist> findByName(@Param("name") String albumName);
}
