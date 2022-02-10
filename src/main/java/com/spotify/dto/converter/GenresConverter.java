package com.spotify.dto.converter;

import com.spotify.dto.response.GenresResponseType;
import com.spotify.entities.Genres;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GenresConverter {
    private final ModelMapper modelMapper = new ModelMapper();
    // @Autowired
    // private AlbumConverter albumConverter;

    // @Autowired
    // private SongConverter songConverter;

    public GenresResponseType convertToDTO(Genres genres) {
        GenresResponseType genresResponseType = new GenresResponseType();
        genresResponseType.setImage(genres.getImage());
        genresResponseType.setGenresName(genres.getGenresName());
        genresResponseType.setId(genres.getId());
        // List<AlbumResponseType> albumResponseTypes = new ArrayList<>();
        // List<Albums> albums = genres.getAlbums();
        // if(albums != null && !albums.isEmpty()){
        //     albums.forEach(item -> {
        //         AlbumResponseType albumResponseType = new AlbumResponseType();
        //         albumResponseType = albumConverter.convertToDTO(item);
        //         albumResponseTypes.add(albumResponseType);
        //     });
        // }
        // if(!albumResponseTypes.isEmpty() && null != albumResponseTypes){
        //     genresResponseType.setAlbums(albumResponseTypes);
        // }
        // List<SongResponseType> songResponseTypes = new ArrayList<>();
        // List<Songs> songs = genres.getSongs();
        // if(songs != null && !songs.isEmpty()){
        //     songs.forEach(item -> {
        //         SongResponseType songResponseType = new SongResponseType();
        //         songResponseType = songConverter.convertToDTONew(item);
        //         songResponseTypes.add(songResponseType);
        //     });
        // }
        // if(!songResponseTypes.isEmpty() && null != songResponseTypes){
        //     genresResponseType.setSongs(songResponseTypes);
        // }
        return genresResponseType;
    }

    public Genres convertToEntity(GenresResponseType genresResponseType) {
        Genres genres = modelMapper.map(genresResponseType, Genres.class);
        return genres;
    }
}
