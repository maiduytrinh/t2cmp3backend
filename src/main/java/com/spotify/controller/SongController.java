package com.spotify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spotify.dto.request.PaginationRequest;
import com.spotify.dto.response.SongResponseType;
import com.spotify.service.SongService;
import com.spotify.service.impl.FileStorageServiceImpl;
import com.spotify.ultils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/songs")
public class SongController {
    private final FileStorageServiceImpl fileStorageService;
    private final SongService songService;
    @Autowired
    public SongController(SongService songService, FileStorageServiceImpl fileStorageService) {
        this.songService = songService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> paginationSongs(@RequestBody PaginationRequest paginationRequest) throws JsonProcessingException {
        ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(songService.paginationSongs(paginationRequest), HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongResponseType> findByIdSong(@PathVariable("id") int id) throws JsonProcessingException {
        ResponseEntity<SongResponseType> pResponse = new ResponseEntity<>(songService.findById(id), HttpStatus.OK);
        return pResponse;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") int id) {
        boolean isDelete = songService.delete(id);
        if (isDelete) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/save")
    public ResponseEntity<SongResponseType> saveSong(@RequestParam("song") String songJson,
                                                     @RequestParam(value = "files") MultipartFile[] files
    ) throws IOException {
        ResponseEntity<SongResponseType> pResponse;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SongResponseType songResponseType = objectMapper.readValue(songJson, SongResponseType.class);
        String mp3 = "";
        String media = "";
        if (null != files) {
            for (int i = 0; i < files.length; i++) {
                if (Utils.getFileExtension(files[i].getOriginalFilename()).equals("mp3")) {
                    mp3 = fileStorageService.storeFile(files[i]);
                } else {
                    media = fileStorageService.storeFile(files[i]);
                }
            }
        }
        songResponseType.setImage(Utils.getUrlFilePathImage(media));
        songResponseType.setMediaUrl(Utils.getUrlFilePathMp3Source(mp3));
        pResponse = new ResponseEntity<>(songService.saveSong(songResponseType), HttpStatus.CREATED);
        return pResponse;
    }

    @PostMapping("/update-song/{id}")
    public ResponseEntity<SongResponseType> updateSong(@RequestParam("song") String songJson,
                                                       @RequestParam(value = "files", required = false) MultipartFile[] files,
                                                       @PathVariable("id") Integer id
    ) throws IOException {
        ResponseEntity<SongResponseType> pResponse;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SongResponseType songResponseType = objectMapper.readValue(songJson, SongResponseType.class);
        songResponseType.setId(id);
        String mp3 = "";
        String media = "";
        System.out.println(songJson);
        if (null != files) {
            for (int i = 0; i < files.length; i++) {
                if (Utils.getFileExtension(files[i].getOriginalFilename()).equals("mp3")) {
                    mp3 = fileStorageService.storeFile(files[i]);
                } else {
                    media = fileStorageService.storeFile(files[i]);
                }
            }
            songResponseType.setImage(Utils.getUrlFilePathImage(media));
            songResponseType.setMediaUrl(Utils.getUrlFilePathMp3Source(mp3));
        }
        pResponse = new ResponseEntity<>(songService.saveSong(songResponseType), HttpStatus.ACCEPTED);
        return pResponse;
    }

    @GetMapping("/top10")
    public ResponseEntity<List<SongResponseType>> getTop10BestSong() throws JsonProcessingException {
        ResponseEntity<List<SongResponseType>> pResponse;
        List<SongResponseType> list = songService.getTop10SongsPopular();
        pResponse = new ResponseEntity<>(list, HttpStatus.OK);
        return pResponse;
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteListSong(@RequestBody List<Integer> listSongId) {
        boolean isDelete = songService.deleteListSong(listSongId);
        if (isDelete) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
