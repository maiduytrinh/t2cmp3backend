package com.spotify.service;

import com.spotify.dto.request.PaginationRequest;
import com.spotify.dto.response.PlayListResponseType;

import java.util.List;
import java.util.Map;

public interface PlayListService extends BaseService<PlayListResponseType, Integer> {
    Map<String, Object> paginationPlayList(PaginationRequest paginationRequest);

    List<PlayListResponseType> getListPlaylistByEmail(String email);

    boolean delSongToPlaylist(int playlistId, int songId);

    void addSongToPlaylist(int playlistId, int songId);
}
