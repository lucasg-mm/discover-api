package com.discover.discoverapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "artists")
@Getter @Setter @NoArgsConstructor
public class Artist {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageURL;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "artists_albums", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private List<Album> albums;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "artists_genres", joinColumns = @JoinColumn(name =  "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "artists_tracks", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    private List<Track> tracks;

    // CONSTRUCTORS
    public Artist(String name, String imageURL, List<Album> albums, List<Genre> genres, List<Track> tracks) {
        this.name = name;
        this.imageURL = imageURL;
        this.albums = albums;
        this.genres = genres;
        this.tracks = tracks;
    }

    // METHODS
    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
