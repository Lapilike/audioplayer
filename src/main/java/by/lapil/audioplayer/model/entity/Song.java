package by.lapil.audioplayer.model.entity;

import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.utils.Genres;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "song")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToMany(mappedBy = "songs")
    private List<Artist> artist;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Genres genre;

    @Column
    private String filePath;

    public Song(CreateSongDto musicDto) {
        this.title = musicDto.getTitle();
        this.genre = musicDto.getGenre();
        this.filePath = musicDto.getFilePath();
    }
}
