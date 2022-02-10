package com.spotify.dto.converter;

import java.util.ArrayList;
import java.util.List;

import com.spotify.dto.response.PlayListResponseType;
import com.spotify.dto.response.PlayListSongResponseType;
import com.spotify.dto.response.SongResponseType;
import com.spotify.entities.PlayListSongs;
import com.spotify.entities.PlayLists;
import com.spotify.entities.Songs;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlaylistConverter {
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private SongConverter songConverter;

    public PlayListResponseType convertToDTO(PlayLists playLists){
        PlayListResponseType playListResponseType = new PlayListResponseType();
        playListResponseType.setId(playLists.getId());
        playListResponseType.setPlaylistName(playLists.getPlaylistName());
        playListResponseType.setUsers(userConverter.convertToDTO(playLists.getUsers()));
        return playListResponseType;
    }

    public PlayListResponseType convertToAll(PlayLists playLists){
        PlayListResponseType playListResponseType = new PlayListResponseType();
        playListResponseType.setId(playLists.getId());
        playListResponseType.setPlaylistName(playLists.getPlaylistName());
        playListResponseType.setUsers(userConverter.convertToDTO(playLists.getUsers()));
        List<PlayListSongResponseType> playListSongResponseTypes = new ArrayList<>();
        List<PlayListSongs> songs = playLists.getPlayListSongs();
        if(songs != null && !songs.isEmpty()){
            songs.forEach(item -> {
                PlayListSongResponseType playListSongResponseType = new PlayListSongResponseType();
                playListSongResponseType.setSongs(songConverter.convertToDTO(item.getPlayListSongId().getSongs()));
                playListSongResponseTypes.add(playListSongResponseType);
            });
        }
        if(!playListSongResponseTypes.isEmpty() && null != playListSongResponseTypes){
            playListResponseType.setSongs(playListSongResponseTypes);
        }
        return playListResponseType;
    }

    public PlayLists convertToEntity(PlayListResponseType playListResponseType) {
        PlayLists playLists = modelMapper.map(playListResponseType, PlayLists.class);
        return playLists;
    }
}
