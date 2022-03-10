package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tracks")
@Getter @Setter @NoArgsConstructor
public class Track {
    // PROPERTIES
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @JsonIgnore
    @Column(name = "lyrics")
    private String lyrics;

    @Column(name = "length")
    private int length;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @JoinTable(name = "artists_tracks", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    // CONSTRUCTORS

    public Track(String title, String lyrics, int length, Album album, Set<Genre> genres, Set<Artist> artists) {
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
