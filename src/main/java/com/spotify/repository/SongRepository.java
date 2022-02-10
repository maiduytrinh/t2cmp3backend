package com.spotify.repository;

import com.spotify.entities.Songs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Songs, Integer> {
    @Query(value = "select  s from Songs s where (s.lyrics like %:search%) or (s.description like %:search%) or (s.title like %:search%)")
    Page<Songs> paginationSongs(Pageable pageable, String search);

    @Query(value = "SELECT * FROM songs order by count_listen desc limit 10", nativeQuery = true)
    List<Songs> getTop10SongPopular();

    @Query(value = "SELECT * FROM songs order by count_listen desc limit 30", nativeQuery = true)
    List<Songs> getTop30SongPopular();
    
    @Query(value = "update songs set count_listen = count_listen + 1 where id =:songId", nativeQuery = true)
    @Modifying
    void updateCountListen(int songId);
}
