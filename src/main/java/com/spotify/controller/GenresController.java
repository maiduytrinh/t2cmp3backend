package com.spotify.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.dto.request.PaginationRequest;
import com.spotify.dto.response.GenresResponseType;
import com.spotify.service.GenresService;
import com.spotify.service.impl.FileStorageServiceImpl;
import com.spotify.ultils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/genres")
@CrossOrigin(origins = "*")
public class GenresController {
    private final GenresService genresService;
    private final FileStorageServiceImpl fileStorageService;

    @Autowired
    public GenresController(GenresService genresService, FileStorageServiceImpl fileStorageService) {
        this.genresService = genresService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> paginationGenres(@RequestBody PaginationRequest paginationRequest) throws JsonProcessingException {
        ResponseEntity<Map<String, Object>> response = new ResponseEntity<>(genresService.paginationGenres(paginationRequest), HttpStatus.OK);
        return response;
    }

    @PostMapping("/save")
    public ResponseEntity<GenresResponseType> saveGenres(@RequestParam("genres") String genres,
                                                         @RequestParam("file") MultipartFile image
    ) throws Exception {
        GenresResponseType genresResponseType = new ObjectMapper().readValue(genres, GenresResponseType.class);
        String imageURL = fileStorageService.storeFile(image);
        genresResponseType.setImage(Utils.getUrlFilePathImage(imageURL));
        return new ResponseEntity<>(genresService.save(genresResponseType), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenresResponseType> findByIdGenres(@PathVariable("id") int id) {
        GenresResponseType genresResponseType = genresService.findById(id);
        return new ResponseEntity<>(genresResponseType, HttpStatus.OK);
    }
    @PostMapping("/album-song/{id}")
    public ResponseEntity<Map<String,Object>> getAlbumAndSongByGenresId(@RequestParam(value = "paginationAlbum") String paginationAlbum,
                                                                        @RequestParam(value = "paginationSong") String paginationSong,
                                                                        @PathVariable("id") int id
                                                                        )throws Exception {
        ResponseEntity<Map<String,Object>>  pResponse;
        PaginationRequest albumRequest = new ObjectMapper().readValue(paginationAlbum,PaginationRequest.class);
        PaginationRequest songRequest = new ObjectMapper().readValue(paginationSong,PaginationRequest.class);
        pResponse = new ResponseEntity<>(genresService.getSongAndAlbumByGenresId(id,albumRequest,songRequest),HttpStatus.OK);
        return pResponse;
    }
}
