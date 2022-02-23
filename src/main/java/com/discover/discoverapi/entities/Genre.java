package com.discover.discoverapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "genres")
@Getter @Setter @NoArgsConstructor
public class Genre {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "albums_genres", joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private List<Album> albums;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "artists_genres", joinColumns = @JoinColumn(name="genre_id"),
            inverseJoinColumns = @JoinColumn(name="artists"))
    private List<Artist> artists;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    private List<Track> tracks;

    // CONSTRUCTORS
    public Genre(String name, List<Album> albums, List<Artist> artists, List<Track> tracks) {
        this.name = name;
        this.albums = albums;
        this.artists = artists;
        this.tracks = tracks;
    }

    // METHODS
    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
