package audioplayer.model.entity;

import audioplayer.model.dto.CreateAlbumDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "album")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"artist", "songs"})
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "album",
            cascade = CascadeType.PERSIST)
    private List<Song> songs;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    public Album(CreateAlbumDto createDto) {
        this.name = createDto.getName();
    }
}
