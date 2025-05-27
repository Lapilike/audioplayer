package audioplayer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import audioplayer.exception.NotFoundException;
import audioplayer.model.dto.CreatePlaylistDto;
import audioplayer.model.dto.PlaylistDto;
import audioplayer.model.entity.Playlist;
import audioplayer.model.entity.Song;
import audioplayer.repository.PlaylistRepository;
import audioplayer.service.impl.PlaylistServiceImpl;
import audioplayer.utils.Genres;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PlaylistServiceTest {
    @Mock
    private PlaylistRepository playlistRepository;

    @Mock
    private SongService songService;

    @Mock
    private ArtistService artistService;

    @InjectMocks
    private PlaylistServiceImpl albumService;
    private Playlist playlist;
    private Song song;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        playlist = new Playlist();
        playlist.setId(1L);
        playlist.setName("Test Album");

        song = new Song();
        song.setId(1L);
        song.setTitle("Test Song");
        song.setGenre(Genres.ROCK);
        song.setFilePath("/path");
        song.getPlaylists().add(playlist);

        playlist.setSongs(new ArrayList<>(List.of(song)));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(playlistRepository.findAll()).thenReturn(List.of(playlist));
        List<PlaylistDto> result = albumService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findByName_ShouldReturnList() {
        when(playlistRepository.findByName(playlist.getName())).thenReturn(List.of(playlist));
        List<PlaylistDto> result = albumService.findByName(playlist.getName());
        assertEquals(1, result.size());
    }

    @Test
    void findByName_ShouldThrowNotFoundException() {
        when(playlistRepository.findByName("Nonexistent name")).thenReturn(Collections.emptyList());
        assertThrows(NotFoundException.class, () -> albumService.findByName("Nonexistent name"));
    }

    @Test
    void create_shouldReturnAlbumDto_whenValidCreateDto() {
        CreatePlaylistDto createDto = new CreatePlaylistDto();
        createDto.setName("new Album");
        createDto.setSongs(List.of(1L));

        Playlist newPlaylist = new Playlist(createDto);
        newPlaylist.setId(2L);
        newPlaylist.setSongs(new ArrayList<>(List.of(song)));

        List<Song> songList = new ArrayList<>(List.of(song));

        when(songService.findAllById(createDto.getSongs())).thenReturn(songList);
        when(playlistRepository.save(any(Playlist.class))).thenReturn(newPlaylist);

        PlaylistDto result = albumService.create(createDto);

        assertNotNull(result);
        assertEquals(createDto.getName(), result.getName());
        assertEquals(2L, result.getId());

        verify(playlistRepository).save(any(Playlist.class));
        verify(artistService).update(anyList());
        verify(songService).update(songList);
    }

    @Test
    void patch_ShouldThrowNotFoundException() {
        CreatePlaylistDto createDto = new CreatePlaylistDto();
        when(playlistRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> albumService.patch(2L, createDto));
    }

    @Test
    void patch_ShouldUpdateAllFields() {
        Song newSong = new Song();
        newSong.setId(2L);
        newSong.setTitle("New Song");

        CreatePlaylistDto dto = new CreatePlaylistDto();
        dto.setName("New Name");
        dto.setSongs(List.of(2L));

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> inv.getArgument(0));

        PlaylistDto result = albumService.patch(1L, dto);

        assertEquals("New Name", result.getName());
        verify(artistService).update(anyList());
        verify(playlistRepository).save(playlist);
    }

    @Test
    void patch_shouldUpdateAlbumName() {
        CreatePlaylistDto dto = new CreatePlaylistDto();
        dto.setName("New Name");

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> inv.getArgument(0));

        PlaylistDto result = albumService.patch(1L, dto);

        assertEquals("New Name", result.getName());
        verify(playlistRepository).save(playlist);
    }

    @Test
    void patch_shouldUpdateSongs() {
        Song newSong = new Song();
        newSong.setId(2L);
        newSong.setTitle("New Song");

        CreatePlaylistDto dto = new CreatePlaylistDto();
        dto.setSongs(List.of(2L));

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        when(songService.findAllById(List.of(2L))).thenReturn(new ArrayList<>(List.of(newSong)));
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> inv.getArgument(0));

        PlaylistDto result = albumService.patch(1L, dto);

        assertEquals(1, result.getSongIds().size());
        assertTrue(result.getSongIds().stream().anyMatch(s -> s.equals(newSong.getId())));
        verify(playlistRepository).save(playlist);
    }

    @Test
    void delete_ShouldThrowNotFoundException() {
        when(playlistRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> albumService.delete(2L));
    }

    @Test
    void delete_shouldRemoveSongsAndDeleteAlbum_whenAlbumFound() {
        List<Song> songList = new ArrayList<>(List.of(song));
        playlist.setSongs(songList);

        when(playlistRepository.findById(1L)).thenReturn(Optional.of(playlist));
        doNothing().when(playlistRepository).deleteById(1L);
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> inv.getArgument(0));

        albumService.delete(1L);

        assertTrue(playlist.getSongs().isEmpty());
        verify(playlistRepository).deleteById(1L);
        verify(playlistRepository).save(playlist);
    }
}


