package by.lapil.audioplayer.repository;

import by.lapil.audioplayer.model.entity.Album;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT a FROM Album a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Album> findByName(@Param("name") String albumName);
}
