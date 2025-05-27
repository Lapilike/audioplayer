package audioplayer.model.entity;

import audioplayer.model.dto.CreateSongDto;
import audioplayer.utils.Genres;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "song")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"artist", "playlists"})
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "songs",
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH,
            }
    )
    private List<Playlist> playlists;

    @ManyToMany(
            mappedBy = "songs",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
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
        this.playlists = new ArrayList<>();
    }
}
