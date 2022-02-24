package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tracks")
@Getter @Setter @NoArgsConstructor
@JsonIgnoreProperties({"album", "genres", "artists"})
public class Track {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "lyrics")
    private String lyrics;

    @Column(name = "length")
    private int length;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonBackReference
    private Album album;

    @ManyToMany
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @ManyToMany
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
