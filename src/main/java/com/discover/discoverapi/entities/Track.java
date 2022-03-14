package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Schema(description = "Represents a track.")
@Entity
@Table(name = "tracks")
@Getter @Setter @NoArgsConstructor
public class Track {
    // PROPERTIES
    @Schema(description = "The track's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Schema(description = "The track's title.")
    @NotEmpty(message = "Track's title should be specified.")
    @Column(name = "title")
    private String title;

    @Schema(description = "The track's length (in seconds).")
    @Min(value = 1, message = "A track should be at least one second long.")
    @NotNull(message = "The album's track should be specified.")
    @Column(name = "length")
    private int length;

    @Schema(description = "The album the track is from.")
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Schema(description = "The genres the track is classified with.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @Schema(description = "The track's artists.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @JoinTable(name = "artists_tracks", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    // CONSTRUCTORS

    public Track(String title, int length, Album album, Set<Genre> genres, Set<Artist> artists) {
        this.title = title;
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
                ", length=" + length +
                '}';
    }
}
