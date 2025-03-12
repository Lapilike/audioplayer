package by.lapil.audioplayer.model.entity;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.utils.Genres;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "music")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany(mappedBy = "artist")
    private Set<Artist> artist;

    @Column(nullable = false)
    private Genres genre;

    @Column
    private String filePath;

    public Song(CreateSongDto musicDto) {
        this.title = musicDto.getTitle();
        this.genre = Genres.parseGenre(musicDto.getGenre().toUpperCase());
        this.filePath = musicDto.getFilePath();
    }
}
