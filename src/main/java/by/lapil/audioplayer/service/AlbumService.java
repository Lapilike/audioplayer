package by.lapil.audioplayer.service;

import by.lapil.audioplayer.model.dto.AlbumDto;
import by.lapil.audioplayer.model.dto.CreateAlbumDto;
import by.lapil.audioplayer.model.entity.Album;
import java.util.List;

public interface AlbumService {
    List<AlbumDto> findAll();

    List<Album> findAllById(List<Long> ids);

    List<AlbumDto> findByName(String name);

    AlbumDto create(CreateAlbumDto createDto);

    AlbumDto update(Long id, CreateAlbumDto createDto);

    void delete(Long id);
}
