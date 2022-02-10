package com.spotify.service.impl;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.spotify.dto.converter.PlaylistConverter;
import com.spotify.dto.request.PaginationRequest;
import com.spotify.dto.response.PlayListResponseType;
import com.spotify.entities.PlayListSongs;
import com.spotify.entities.PlayLists;
import com.spotify.exception.NotFoundEntityException;
import com.spotify.repository.PlayListRepository;
import com.spotify.repository.PlayListSongRepository;
import com.spotify.service.PlayListService;
import com.spotify.ultils.Constraints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;



@Service
public class PlayListServiceImpl implements PlayListService{
    private PlayListRepository playListRepository;
    private PlaylistConverter playlistConverter;
    private PlayListSongRepository playListSongRepository;


    @Autowired
    public PlayListServiceImpl(PlayListRepository playListRepository, PlaylistConverter playlistConverter,
            PlayListSongRepository playListSongRepository) {
        this.playListRepository = playListRepository;
        this.playlistConverter = playlistConverter;
        this.playListSongRepository = playListSongRepository;
    }

    @Override
    public PlayListResponseType save(PlayListResponseType playListResponseType) throws Exception {
        PlayLists playLists = playlistConverter.convertToEntity(playListResponseType);
        PlayListResponseType playListResponseType1 = playlistConverter.convertToDTO(playListRepository.save(playLists));
        return playListResponseType1;
    }

    @Override
    public PlayListResponseType update(PlayListResponseType t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PlayListResponseType findById(Integer id) {
        Optional<PlayLists> playLists = playListRepository.findById(id);
        if (playLists.isPresent()) {
            return playlistConverter.convertToAll(playLists.get());
        }
        throw new NotFoundEntityException(Constraints.VALIDATE_NOT_FOUND);
    }

    @Override
    public boolean delete(Integer id) {
        Optional<PlayLists> playlist = playListRepository.findById(id);
        if (playlist.isPresent()) {
            playListRepository.deleteById(id);
            return true;
        }
        throw new NotFoundEntityException(Constraints.VALIDATE_NOT_FOUND);
    }


    @Override
    public Map<String, Object> paginationPlayList(PaginationRequest request) {
        Pageable pageable = null;
        Map<String, Object> result = new HashMap<>();
        if (request.getPage() > 0) {
            pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        }
        if (request.getOrder().equals("asc")) {
            pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.getField()).ascending());
        }
        if (request.getOrder().equals("desc")) {
            pageable = PageRequest.of(request.getPage() - 1, request.getSize(), Sort.by(request.getField()).descending());
        }
        Page<PlayLists> playlistsPage = playListRepository.paginationPlaylist(pageable, request.getSearch());
        List<PlayListResponseType> playListResponseTypes = playlistsPage.toList().stream().map(playlists -> playlistConverter.convertToDTO(playlists)).collect(Collectors.toList());
        result.put("playlists", playListResponseTypes);
        result.put("totalPages", playlistsPage.getTotalPages());
        result.put("totalElements", playlistsPage.getTotalElements());
        result.put("currentPage", request.getPage());
        return result;
    }


    @Override
    public List<PlayListResponseType> getListPlaylistByEmail(String email) {
        List<PlayLists> playLists = playListRepository.getAllByEmail(email);
        List<PlayListResponseType> playListResponseTypes = playLists.stream().map(playlist -> playlistConverter.convertToDTO(playlist)).collect(Collectors.toList());
        return playListResponseTypes;
    }


    @Override
    public boolean delSongToPlaylist(int playlistId, int songId) {
        int Del = playListRepository.DelSong(playlistId, songId);
        if(Del == 1){
            return true;
        }
        throw new NotFoundEntityException(Constraints.VALIDATE_NOT_FOUND);
    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) {
        playListRepository.AddSong(playlistId, songId);
    }
    
}
