package audioplayer.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import audioplayer.exception.NotFoundException;
import audioplayer.model.dto.ArtistDto;
import audioplayer.model.dto.CreateArtistDto;
import audioplayer.model.dto.UpdateArtistDto;
import audioplayer.model.entity.Playlist;
import audioplayer.model.entity.Artist;
import audioplayer.model.entity.Song;
import audioplayer.repository.ArtistRepository;
import audioplayer.service.impl.ArtistServiceImpl;
import audioplayer.utils.Genres;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ArtistServiceTest {
    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private SongService songService;

    @Mock
    private PlaylistService playlistService;

    @InjectMocks
    private ArtistServiceImpl artistService;
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
    void findAll_ShouldReturnList() {
        when(artistRepository.findAll()).thenReturn(List.of(artist));
        List<Artist> result = artistService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findById_ShouldReturnArtist() {
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));
        Artist result = artistService.findById(1L);
        assertEquals(artist, result);
    }

    @Test
    void findById_ShouldReturnNotFound() {
        assertThatThrownBy(() -> artistService.findById(2L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.ARTIST_NOT_FOUND);
    }

    @Test
    void findAllById_ShouldReturnList() {
        when(artistRepository.findAllById(List.of(artist.getId()))).thenReturn(List.of(artist));
        List<Artist> result = artistService.findAllById(List.of(artist.getId()));
        assertEquals(1, result.size());
    }

    @Test
    void findByName_ShouldReturnArtistDtos() {
        when(artistRepository.findByName(artist.getName())).thenReturn(List.of(artist));
        List<ArtistDto> result = artistService.findByName(artist.getName());
        assertEquals(1, result.size());
    }

    @Test
    void findByName_ShouldThrowNotFoundException() {
        assertThatThrownBy(() -> artistService.findByName(""))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.ARTIST_NOT_FOUND);
    }

    @Test
    void findAllByName_ShouldReturnArtistDtos() {
        when(artistRepository.findAllByNameInIgnoreCase(
                List.of(artist.getName()))).thenReturn(List.of(artist));
        List<ArtistDto> result = artistService.findAllByName(List.of(artist.getName()));
        assertEquals(1, result.size());
    }

    @Test
    void findAllByName_ShouldThrowNotFoundException() {
        assertThatThrownBy(() -> artistService.findAllByName(List.of("")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.ARTIST_NOT_FOUND);
    }

    @Test
    void update_ShouldUpdateArtistAndReturnArtistDto() {
        String newName = "New name";
        List<Long> songIds = List.of(2L);

        Song song1 = new Song();
        song1.setTitle("New Song");
        song1.setId(2L);
        song1.setArtist(new ArrayList<>());

        UpdateArtistDto updateDto = new UpdateArtistDto();
        updateDto.setName(newName);
        updateDto.setSongIds(songIds);

        List<Song> songList = List.of(song1);

        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(songService.findAllById(List.of(song1.getId()))).thenReturn(songList);
        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistDto result = artistService.update(artist.getId(), updateDto);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(1, result.getSongs().size());
        assertTrue(result.getSongs().contains(song1.getTitle()));

        assertTrue(song.getArtist().contains(artist));

        verify(artistRepository).findById(1L);
        verify(songService).findAllById(songIds);
        verify(artistRepository).save(artist);
    }

    @Test
    void update_ShouldThrowNotFoundException() {
        UpdateArtistDto updateDto = new UpdateArtistDto();

        assertThatThrownBy(() -> artistService.update(2L, updateDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.ARTIST_NOT_FOUND);
    }

    @Test
    void updateList_ShouldReturnArtistDtos() {
        List<Artist> artistList = List.of(artist);
        when(artistRepository.saveAll(artistList)).thenReturn(artistList);

        List<ArtistDto> result = artistService.update(artistList);
        assertEquals(1, result.size());
    }

    @Test
    void updateList_ShouldThrowNotFoundException() {
        List<Artist> artistList = new ArrayList<>();

        assertThatThrownBy(() -> artistService.update(artistList))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.ARTIST_NOT_FOUND);
    }

    @Test
    void patch_ShouldUpdateAllFields() {
        String newName = "New name";
        List<Long> songIds = List.of(2L);

        Song song1 = new Song();
        song1.setTitle("New Song");
        song1.setId(2L);
        song1.setArtist(new ArrayList<>());

        UpdateArtistDto updateDto = new UpdateArtistDto();
        updateDto.setName(newName);
        updateDto.setSongIds(songIds);

        List<Song> songList = List.of(song1);

        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(songService.findAllById(List.of(song1.getId()))).thenReturn(songList);
        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistDto result = artistService.patch(artist.getId(), updateDto);

        assertNotNull(result);
        assertEquals(newName, result.getName());
        assertEquals(1, result.getSongs().size());
        assertTrue(result.getSongs().contains(song1.getTitle()));

        assertTrue(song.getArtist().contains(artist));

        verify(artistRepository).findById(1L);
        verify(songService).findAllById(songIds);
        verify(artistRepository).save(artist);
    }

    @Test
    void patch_ShouldUpdateName() {
        String newName = "New name";

        UpdateArtistDto updateDto = new UpdateArtistDto();
        updateDto.setName(newName);

        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistDto result = artistService.patch(artist.getId(), updateDto);

        assertNotNull(result);
        assertEquals(newName, result.getName());

        verify(artistRepository).findById(1L);
        verify(artistRepository).save(artist);
    }

    @Test
    void patch_ShouldUpdateSongs() {
        List<Long> songIds = List.of(2L);

        Song song1 = new Song();
        song1.setTitle("New Song");
        song1.setId(2L);
        song1.setArtist(new ArrayList<>());

        List<Song> songList = List.of(song1);

        UpdateArtistDto updateDto = new UpdateArtistDto();
        updateDto.setSongIds(songIds);

        when(artistRepository.findById(artist.getId())).thenReturn(Optional.of(artist));
        when(songService.findAllById(List.of(song1.getId()))).thenReturn(songList);
        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistDto result = artistService.patch(artist.getId(), updateDto);

        assertNotNull(result);
        assertEquals(1, result.getSongs().size());
        assertTrue(result.getSongs().contains(song1.getTitle()));

        assertTrue(song.getArtist().contains(artist));

        verify(artistRepository).findById(1L);
        verify(songService).findAllById(songIds);
        verify(artistRepository).save(artist);
    }

    @Test
    void create_ShouldReturnArtistDto() {
        CreateArtistDto createDto = new CreateArtistDto();
        createDto.setName("New Artist");

        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ArtistDto result = artistService.create(createDto);

        assertNotNull(result);
        assertEquals(createDto.getName(), result.getName());
    }

    @Test
    void testCreateBulk_shouldCreateArtistsAndReturnDtos() {
        // given
        CreateArtistDto dto1 = new CreateArtistDto("Artist 1");
        CreateArtistDto dto2 = new CreateArtistDto("Artist 2");
        List<CreateArtistDto> createList = List.of(dto1, dto2);

        Artist artist1 = new Artist(dto1);
        artist1.setId(1L);
        Artist artist2 = new Artist(dto2);
        artist2.setId(2L);
        List<Artist> savedArtists = List.of(artist1, artist2);

        when(artistRepository.saveAll(anyList())).thenReturn(savedArtists);

        // when
        List<ArtistDto> result = artistService.createBulk(createList);

        // then
        assertEquals(2, result.size());
        assertEquals("Artist 1", result.get(0).getName());
        assertEquals("Artist 2", result.get(1).getName());

        verify(artistRepository, times(1)).saveAll(anyList());
    }

    @Test
    void deleteById_shouldThrowNotFoundException() {
        // given
        Long artistId = 1L;
        when(artistRepository.findById(artistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.deleteById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining(NotFoundException.ARTIST_NOT_FOUND);
    }

    @Test
    void deleteById_shouldDeleteAlbumsAndSongs() {
        Playlist playlist = new Playlist();
        playlist.setId(1L);

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        artistService.deleteById(1L);

        verify(playlistService).delete(playlist.getId());
        verify(songService).deleteById(song.getId());
        verify(artistRepository).delete(artist);
    }

    @Test
    void deleteById_shouldNotDeleteSongs_whenSongHasMultipleArtists() {
        Artist artist1 = new Artist();
        artist1.setId(1L);
        artist1.setName("Artist 1");

        song.getArtist().add(artist1);
        artist1.setSongs(new ArrayList<>(List.of(song)));

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        artistService.deleteById(1L);

        verify(songService, never()).deleteById(anyLong());
    }

    @Test
    void deleteById_shouldClearSongsAndAlbums_whenArtistDeleted() {
        Playlist playlist = new Playlist();
        playlist.setId(10L);

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        artistService.deleteById(1L);

        assertTrue(artist.getSongs().isEmpty());
    }
}
