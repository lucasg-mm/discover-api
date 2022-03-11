package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Schema(description = "Represents a genre.")
@Entity
@Table(name = "genres")
@Getter @Setter @NoArgsConstructor
public class Genre {
    // PROPERTIES
    @Schema(description = "The genre's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Schema(description = "The genre's name.")
    @NotEmpty(message = "Genre's name shouldn't be empty.")
    @Column(name = "name")
    private String name;

    @Schema(description = "The albums classified with the genre.")
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "albums_genres", joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private Set<Album> albums;

    @Schema(description = "The artists classified with the genre.")
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "artists_genres", joinColumns = @JoinColumn(name="genre_id"),
            inverseJoinColumns = @JoinColumn(name="artist_id"))
    private Set<Artist> artists;

    @Schema(description = "The tracks classified with the genre.")
    @JsonIgnore
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
}
