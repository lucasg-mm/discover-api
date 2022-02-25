package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter @Setter @NoArgsConstructor
@JsonIgnoreProperties({ "albums", "artists", "tracks" })
public class Genre {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "albums_genres", joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private Set<Album> albums;

    @ManyToMany
    @JoinTable(name = "artists_genres", joinColumns = @JoinColumn(name="genre_id"),
            inverseJoinColumns = @JoinColumn(name="artist_id"))
    private Set<Artist> artists;


    @ManyToMany
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    private Set<Track> tracks;

    // CONSTRUCTORS
    public Genre(String name, Set<Album> albums, Set<Artist> artists, Set<Track> tracks) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Genre genre = (Genre) o;

        return name.equals(genre.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
