package com.spotify.controller;

import com.spotify.dto.request.PaginationRequest;
import com.spotify.dto.response.PlayListResponseType;
import com.spotify.service.PlayListService;
import com.spotify.ultils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/playlist")
@CrossOrigin(origins = "*")
public class PlaylistController {
    private final PlayListService playListService;

    @Autowired
    public PlaylistController(PlayListService playListService) {
        this.playListService = playListService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> paginationAlbums(@RequestBody PaginationRequest paginationRequest) throws JsonProcessingException {
        ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(playListService.paginationPlayList(paginationRequest), HttpStatus.OK);
        return response;
    }

    @GetMapping("/all/{email}")
    public ResponseEntity<List<PlayListResponseType>> findAlbumSongByAlbumId(@PathVariable("email") String email) throws JsonProcessingException {
        ResponseEntity<List<PlayListResponseType>>  pResponse;
        pResponse = new ResponseEntity<>(playListService.getListPlaylistByEmail(email), HttpStatus.OK);
        return pResponse;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayListResponseType> findByIdGenres(@PathVariable("id") int id) {
        PlayListResponseType playListResponseType = playListService.findById(id);
        return new ResponseEntity<>(playListResponseType, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<PlayListResponseType> savePlayList(@RequestParam("playlist") String playlist
    ) throws Exception {
        PlayListResponseType playListResponseType = new ObjectMapper().readValue(playlist, PlayListResponseType.class);
        return new ResponseEntity<>(playListService.save(playListResponseType), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePlaylist(@PathVariable("id") int id) {
        boolean isDelete = playListService.delete(id);
        if (isDelete) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/{idPlayList}/{idSong}")
    public ResponseEntity<HttpStatus> delSongToPlaylist(@PathVariable("idPlayList") int idPlayList, @PathVariable("idSong") int idSong) {
        boolean isDelete = playListService.delSongToPlaylist(idPlayList, idSong);
        if (isDelete) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/{idPlayList}/{idSong}")
    public ResponseEntity<HttpStatus> addSongToPlaylist(@PathVariable("idPlayList") int idPlayList, @PathVariable("idSong") int idSong) {
        playListService.addSongToPlaylist(idPlayList, idSong);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
