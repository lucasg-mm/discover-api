package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "artists")
@Getter @Setter @NoArgsConstructor
public class Artist {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageURL;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "artists_albums", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private Set<Album> albums;

    @JsonIgnore
    @Getter(onMethod_=@JsonProperty)
    @ManyToMany
    @JoinTable(name = "artists_genres", joinColumns = @JoinColumn(name =  "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "artists_tracks", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    private Set<Track> tracks;

    // CONSTRUCTORS
    public Artist(String name, String imageURL, Set<Album> albums, Set<Genre> genres, Set<Track> tracks) {
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
