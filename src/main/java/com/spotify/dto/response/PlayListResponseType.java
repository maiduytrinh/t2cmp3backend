package com.spotify.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayListResponseType {
    private Integer id;
    private String playlistName;
    private UserResponseType users;
    private List<PlayListSongResponseType> songs;
}
