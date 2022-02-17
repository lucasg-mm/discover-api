package com.discover.discoverapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tracks")
@Getter @Setter @NoArgsConstructor
public class Track {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "lyrics")
    private String lyrics;

    @Column(name = "length")
    private int length;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Artist> artists;

    // CONSTRUCTORS
    public Track(String title, String lyrics, int length, Album album, List<Genre> genres, List<Artist> artists) {
        this.title = title;
        this.lyrics = lyrics;
        this.length = length;
        this.album = album;
        this.genres = genres;
        this.artists = artists;
    }

    // METHODS
    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", length=" + length +
                '}';
    }
}
