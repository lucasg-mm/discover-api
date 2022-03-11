package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Schema(description = "Represents an artist.")
@Entity
@Table(name = "artists")
@Getter @Setter @NoArgsConstructor
public class Artist {
    // PROPERTIES
    @Schema(description = "The artist's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Schema(description = "The artist's name.")
    @Column(name = "name")
    private String name;

    @Schema(description = "Where the artist's image is located.")
    @JsonIgnore
    @Column(name = "image_path")
    private String imagePath;

    @Schema(description = "The name of the artist's image file.")
    @JsonIgnore
    @Column(name = "image_file_name")
    private String imageFileName;

    @Schema(description = "The artist's albums.")
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "artists_albums", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id"))
    private Set<Album> albums;

    @Schema(description = "The genres the artist is classified with.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @JoinTable(name = "artists_genres", joinColumns = @JoinColumn(name =  "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @Schema(description = "The artist's tracks.")
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "artists_tracks", joinColumns = @JoinColumn(name = "artist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id"))
    private Set<Track> tracks;

    // CONSTRUCTORS
    public Artist(String name, String imagePath, String imageFileName, Set<Album> albums, Set<Genre> genres, Set<Track> tracks) {
        this.name = name;
        this.imagePath = imagePath;
        this.imageFileName = imageFileName;
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
                ", imagePath='" + imagePath + '\'' +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }
}
