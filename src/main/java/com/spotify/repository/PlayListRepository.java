package com.spotify.repository;

import java.util.List;

import com.spotify.entities.PlayLists;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PlayListRepository extends JpaRepository<PlayLists, Integer> {
    @Query(value = "SELECT A from PlayLists A where A.playlistName like %:search%")
    Page<PlayLists> paginationPlaylist(Pageable pageable, String search);
    @Query(value = "SELECT A from PlayLists A where A.users.email =:email")
    List<PlayLists> getAllByEmail(String email);
    @Query(value = "DELETE FROM PlayListSongs WHERE playListSongId.playLists.id =:playlistId and playListSongId.songs.id =:songId")
    @Modifying
    int DelSong(int playlistId, int songId);
    @Query(value = "INSERT INTO `t2cmp3db`.`play_list_songs` (`play_list_id`, `songs_id`) VALUES (:playlistId, :songId)", nativeQuery = true)
    @Modifying
    int AddSong(int playlistId, int songId);
}
