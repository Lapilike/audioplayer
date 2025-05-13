package audioplayer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import audioplayer.cache.Cache;
import audioplayer.exception.NotFoundException;
import audioplayer.model.dto.ArtistDto;
import audioplayer.model.dto.CreateSongDto;
import audioplayer.model.dto.SongDto;
import audioplayer.model.entity.Album;
import audioplayer.model.entity.Artist;
import audioplayer.model.entity.Song;
import audioplayer.repository.SongRepository;
import audioplayer.service.impl.SongServiceImpl;
import audioplayer.utils.Genres;
import audioplayer.utils.SongsProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "songs.dir=src/test/resources"
})
class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @Mock
    private ArtistService artistService;

    @Mock
    private Cache<List<Song>> cache;

    @Mock
    private SongsProperties songsProperties;

    @InjectMocks
    private SongServiceImpl songService;

    private Artist artist;
    private Song song;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        artist = new Artist();
        artist.setId(1L);
        artist.setName("Test Artist");

        song = new Song();
        song.setId(1L);
        song.setTitle("Test Song");
        song.setGenre(Genres.ROCK);
        song.setFilePath("/path");
        song.setArtist(new ArrayList<>());
        song.getArtist().add(artist);

        List<Song> artistSongs = new ArrayList<>();
        artistSongs.add(song);
        artist.setSongs(artistSongs);
    }

    @Test
    void create_ShouldSaveSongAndReturnDto() {
        // given
        CreateSongDto createDto = new CreateSongDto();
        createDto.setTitle("Test Song");
        createDto.setGenre(Genres.POP);
        createDto.setFilePath("/music/test.mp3");
        List<Long> artistIds = List.of(1L);
        createDto.setArtists(artistIds);

        when(artistService.findAllById(artistIds)).thenReturn(List.of(artist));
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        SongDto result = songService.create(createDto);

        // then
        assertThat(result.getTitle()).isEqualTo("Test Song");
        assertThat(result.getGenre()).isEqualTo(Genres.POP);
        assertThat(result.getFilePath()).isEqualTo("/music/test.mp3");
        assertThat(result.getArtists()).contains("Test Artist");

        ArgumentCaptor<Song> songCaptor = ArgumentCaptor.forClass(Song.class);
        verify(songRepository).save(songCaptor.capture());
        Song savedSong = songCaptor.getValue();
        assertThat(savedSong.getArtist()).containsExactly(artist);
        assertThat(artist.getSongs()).contains(savedSong);
    }

    @Test
    void findByCriteria_ShouldReturnFromCacheIfPresent() {
        String title = "Test";
        String artistName = "Artist";
        String cacheKey = title + "-" + artistName;

        when(cache.get(cacheKey)).thenReturn(List.of(song));

        List<SongDto> result = songService.findByCriteria(title, artistName);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Song");
        verify(cache).get(cacheKey);
        verifyNoInteractions(songRepository);
    }

    @Test
    void findByCriteria_ShouldQueryRepositoryAndCacheResult() {
        String title = "Test";
        String artistName = "Artist";
        String cacheKey = title + "-" + artistName;

        when(cache.get(cacheKey)).thenReturn(null);
        when(songRepository.findByCriteria(artistName, title)).thenReturn(List.of(song));

        List<SongDto> result = songService.findByCriteria(title, artistName);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Song");
        verify(cache).put(cacheKey, List.of(song));
        verify(cache).trackKey(1L, cacheKey);
    }

    @Test
    void findByCriteria_ShouldThrowIfBothParamsEmpty() {
        assertThatThrownBy(() -> songService.findByCriteria("", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Необходимо указать хотя бы один параметр поиска");
    }

    @Test
    void findByCriteria_ShouldThrowIfNoSongsFound() {
        String title = "None";
        String unknownArtist = "Unknown";

        when(cache.get(title + "-" + unknownArtist)).thenReturn(null);
        when(songRepository.findByCriteria(unknownArtist, title)).thenReturn(List.of());

        assertThatThrownBy(() -> songService.findByCriteria(title, unknownArtist))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Song not found");
    }

    @Test
    void findByCriteria_ShouldThrowIfEmpty() {
        assertThrows(IllegalArgumentException.class, () -> songService.findByCriteria(null, null));
    }

    @Test
    void findById_ShouldReturnSong() {
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        Song result = songService.findById(1L);
        assertNotNull(result);
        assertEquals("Test Song", result.getTitle());
    }

    @Test
    void findById_ShouldThrowNotFound() {
        when(songRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> songService.findById(99L));
    }

    @Test
    void patch_ShouldUpdateTitle() {
        CreateSongDto createDto = new CreateSongDto();
        createDto.setTitle("Updated Title");

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        SongDto result = songService.patch(1L, createDto);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getGenre()).isEqualTo(Genres.ROCK);
        assertThat(result.getFilePath()).isEqualTo("/path");

        verify(songRepository).save(song);
    }

    @Test
    void patch_ShouldUpdateArtist() {
        CreateSongDto createDto = new CreateSongDto();
        createDto.setArtists(List.of(1L, 2L));

        Artist newArtist = new Artist();
        newArtist.setName("New Artist");
        newArtist.setId(2L);
        newArtist.setSongs(new ArrayList<>());

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(artistService.findAllById(List.of(1L, 2L))).thenReturn(new ArrayList<>(List.of(newArtist, artist)));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        songService.patch(1L, createDto);
        SongDto result = new SongDto(songService.findById(1L));

        assertThat(result.getTitle()).isEqualTo("Test Song");
        assertThat(result.getGenre()).isEqualTo(Genres.ROCK);
        assertThat(result.getFilePath()).isEqualTo("/path");
        assertThat(result.getArtists()).contains("New Artist");

        verify(songRepository).save(song);
    }

    @Test
    void patch_ShouldUpdateFilePath() {
        CreateSongDto createDto = new CreateSongDto();
        createDto.setFilePath("/new/path");

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        SongDto result = songService.patch(1L, createDto);

        assertThat(result.getTitle()).isEqualTo("Test Song");
        assertThat(result.getGenre()).isEqualTo(Genres.ROCK);
        assertThat(result.getFilePath()).isEqualTo("/new/path");

        verify(songRepository).save(song);
    }

    @Test
    void patch_ShouldUpdateGenre() {
        CreateSongDto createDto = new CreateSongDto();
        createDto.setGenre(Genres.POP);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        SongDto result = songService.patch(1L, createDto);

        assertThat(result.getTitle()).isEqualTo("Test Song");
        assertThat(result.getGenre()).isEqualTo(Genres.POP);
        assertThat(result.getFilePath()).isEqualTo("/path");

        verify(songRepository).save(song);
    }

    @Test
    void patch_ShouldUpdateAllFields() {
        CreateSongDto createDto = new CreateSongDto();
        createDto.setTitle("New Title");
        createDto.setFilePath("/new/path");
        createDto.setGenre(Genres.POP);
        createDto.setArtists(List.of(2L));

        Artist newArtist = new Artist();
        newArtist.setName("New Artist");
        newArtist.setId(2L);
        newArtist.setSongs(new ArrayList<>());

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));
        when(artistService.findAllById(List.of(2L))).thenReturn(new ArrayList<>(List.of(newArtist)));
        when(songRepository.save(any(Song.class))).thenReturn(song);

        SongDto result = songService.patch(1L, createDto);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getGenre()).isEqualTo(Genres.POP);
        assertThat(result.getFilePath()).isEqualTo("/new/path");
        assertThat(result.getArtists()).contains("New Artist");

        verify(songRepository).save(song);
    }

    @Test
    void patch_ShouldThrowNotFoundIfSongDoesNotExist() {
        CreateSongDto createDto = new CreateSongDto();
        createDto.setTitle("New Title");

        when(songRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> songService.patch(1L, createDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Song not found");
    }

    @Test
    void update_ShouldUpdateSongAndReturnDtoCached() {
        Long songId = 1L;

        CreateSongDto createDto = new CreateSongDto();
        createDto.setTitle("Updated Song");
        createDto.setGenre(Genres.ROCK);
        createDto.setArtists(List.of(1L));

        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(artistService.findAllById(List.of(1L))).thenReturn((new ArrayList<>(List.of(artist))));
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<String> keys = Set.of("key1");
        when(cache.getKeys(songId)).thenReturn(keys);
        when(cache.get("key1")).thenReturn(new ArrayList<>(List.of(song)));

        SongDto result = songService.update(songId, createDto);

        assertThat(result.getTitle()).isEqualTo("Updated Song");
        assertThat(result.getGenre()).isEqualTo(Genres.ROCK);
    }

    @Test
    void update_ShouldUpdateSongAndReturnDtoNotCached() {
        Long songId = 1L;

        CreateSongDto createDto = new CreateSongDto();
        createDto.setTitle("Updated Song");
        createDto.setGenre(Genres.ROCK);
        createDto.setArtists(List.of(1L));

        when(songRepository.findById(songId)).thenReturn(Optional.of(song));
        when(artistService.findAllById(List.of(1L))).thenReturn((new ArrayList<>(List.of(artist))));
        when(songRepository.save(any(Song.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(cache.getKeys(songId)).thenReturn(null);

        SongDto result = songService.update(songId, createDto);

        assertThat(result.getTitle()).isEqualTo("Updated Song");
        assertThat(result.getGenre()).isEqualTo(Genres.ROCK);
    }

    @Test
    void update_ShouldThrowNotFoundIfSongDoesNotExist() {
        List<Song> songs = null;

        assertThatThrownBy(() -> songService.update(songs))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.SONG_NOT_FOUND);
    }

    @Test
    void update_ShouldReturnList() {
        List<Song> songs = new ArrayList<>(List.of(song));
        when(songRepository.saveAll(songs)).thenReturn(songs);
        List<SongDto> result = songService.update(songs);

        assertEquals(1, result.size());
    }

    @Test
    void findAll_ShouldReturnList() {
        when(songRepository.findAll()).thenReturn(List.of(song));
        List<Song> result = songService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findAllById_ShouldReturnList() {
        when(songRepository.findAllById(List.of(1L))).thenReturn(List.of(song));
        List<Song> result = songService.findAllById(List.of(1L));
        assertEquals(1, result.size());
    }

    @Test
    void findAllById_ShouldThrowNotFound() {
        assertThatThrownBy(() -> songService.findAllById(List.of(1L)))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.SONG_NOT_FOUND);
    }

    @Test
    void deleteById_ShouldRemoveSong() {
        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        songService.deleteById(1L);

        verify(songRepository).deleteById(1L);
        verify(cache).remove(1L);
    }

    @Test
    void deleteById_ShouldRemoveSongWithoutAlbum() {
        Album album = new Album();
        album.setName("Album");
        album.setSongs(new ArrayList<>(List.of(song)));
        song.setAlbum(album);

        when(songRepository.findById(1L)).thenReturn(Optional.of(song));

        songService.deleteById(1L);

        verify(songRepository).deleteById(1L);
        verify(cache).remove(1L);
        assertThat(album.getSongs()).isEmpty();
    }

    @Test
    void parseAll_shouldThrowNotFoundExceptionWhenDirectoryDoesNotExist() {
        when(songsProperties.getDir()).thenReturn("invalid/directory/path");

        // Действие и проверка: метод должен выбросить исключение
        assertThrows(NotFoundException.class, () -> songService.parseAll());
    }

    @Test
    void parseAll_shouldLogInfoWhenNoMusicFiles() {
        when(songsProperties.getDir()).thenReturn("src/test/resources/empty-directory");

        // Действие и проверка: метод должен выбросить исключение
        assertThrows(NotFoundException.class, () -> songService.parseAll());
    }

    @Test
    void parseAll_shouldProcessFilesCorrectly() {
        // Мокируем путь к папке с музыкальными файлами
        when(songsProperties.getDir()).thenReturn("src/test/resources");

        Artist parseArtist = new Artist();
        parseArtist.setName("Artist");
        parseArtist.setSongs(new ArrayList<>());

        // Моки для других зависимостей
        when(artistService.createBulk(any())).thenReturn(null);
        when(artistService.findAllByName(anyList())).thenReturn(List.of(new ArtistDto()));
        when(artistService.findAllById(anyList())).thenReturn(List.of(parseArtist));
        when(songRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.<Song>getArgument(0));

        // Действие: вызываем метод
        songService.parseAll();

        // Проверка: должен быть вызван метод saveAll для песен
        verify(songRepository, times(2)).saveAll(anyList());
    }
}
