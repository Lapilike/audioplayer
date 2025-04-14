package by.lapil.audioplayer;

import by.lapil.audioplayer.cache.Cache;
import by.lapil.audioplayer.utils.Genres;
import by.lapil.audioplayer.exception.IncorrectGenreException;
import by.lapil.audioplayer.exception.NotFoundException;
import by.lapil.audioplayer.model.dto.CreateSongDto;
import by.lapil.audioplayer.model.dto.SongDto;
import by.lapil.audioplayer.model.entity.Artist;
import by.lapil.audioplayer.model.entity.Song;
import by.lapil.audioplayer.repository.SongRepository;
import by.lapil.audioplayer.service.ArtistService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private ArtistService artistService;

    @Mock
    private Cache<List<Song>> cache;

    private AutoCloseable closeable;

    @InjectMocks
    private by.lapil.audioplayer.service.SongService songService;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void create_ShouldSaveSongAndReturnDto() {
        CreateSongDto dto = new CreateSongDto();
        dto.setTitle("Test Song");
        dto.setGenre(Genres.ROCK);
        dto.setFilePath("/music/test.mp3");
        dto.setArtists(List.of(1L));

        Artist artist = new Artist();
        artist.setId(1L);
        artist.setSongs(new ArrayList<>());

        when(artistService.findAllById(any())).thenReturn(List.of(artist));
        when(songRepository.save(any(Song.class))).thenAnswer(i -> i.getArguments()[0]);

        SongDto result = songService.create(dto);

        assertThat(result.getTitle()).isEqualTo("Test Song");
        verify(songRepository).save(any(Song.class));
    }

    @Test
    void findAll_ShouldReturnListOfSongs() {
        when(songRepository.findAll()).thenReturn(List.of(new Song()));
        List<Song> result = songService.findAll();
        assertThat(result).hasSize(1);
    }

    @Test
    void findById_WhenFound_ShouldReturnSong() {
        Song song = new Song();
        song.setId(1L);
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        Song result = songService.findById(1L);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_WhenNotFound_ShouldThrowException() {
        when(songRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> songService.findById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void update_WhenInvalidDto_ShouldThrowBadRequest() {
        CreateSongDto dto = new CreateSongDto();
        assertThatThrownBy(() -> songService.update(1L, dto))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void patch_WhenGenreIncorrect_ShouldThrowException() {
        Song song = new Song();
        song.setId(1L);
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        CreateSongDto dto = new CreateSongDto();
        dto.setGenre(null);

        assertThatThrownBy(() -> songService.patch(1L, dto))
                .isInstanceOf(IncorrectGenreException.class);
    }

    @Test
    void deleteById_WhenSongExists_ShouldDelete() {
        Song song = new Song();
        song.setId(1L);
        song.setArtist(new ArrayList<>());
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        songService.deleteById(1L);
        verify(songRepository).deleteById(1L);
        verify(cache).remove(1L);
    }
}

