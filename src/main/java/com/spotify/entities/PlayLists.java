package com.spotify.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "play_lists")
@Entity

public class PlayLists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "play_list_name")
    private String playlistName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;
    @OneToMany(mappedBy = "playListSongId.playLists", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayListSongs> playListSongs;
}
