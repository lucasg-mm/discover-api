package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Represents an album.")
@Entity
@Table(name = "albums")
@Getter @Setter @NoArgsConstructor
public class Album {

    // PROPERTIES
    @Schema(description = "The album's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Schema(description = "The album's title.")
    @NotEmpty(message = "The album's title should be specified.")
    @Column(name = "title")
    private String title;

    @Schema(type="string", description = "The album's release date (dd/MM/yyyy format).", example = "26/08/1999")
    @NotNull
    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Schema(description = "The album's cover art path.")
    @JsonIgnore
    @Column(name = "cover_art_path")
    private String coverArtPath;

    @Schema(description = "The album's cover art file name.")
    @JsonIgnore
    @Column(name = "cover_art_file_name")
    private String coverArtFileName;

    @Schema(description = "The album's label.")
    @Column(name = "label")
    private String label;

    @Schema(description = "The album's length (in seconds).")
    @NotNull(message = "The album's length should be specified.")
    @Min(value = 1, message = "An album should be at least one second long.")
    @Column(name = "length")
    private int length;

    @Schema(description = "The album's artists.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @JoinTable(name = "artists_albums", joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    @Schema(description = "The genres the album is classified with.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToMany
    @JoinTable(name = "albums_genres", joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @Schema(description = "The album's tracks.")
    @JsonIgnore
    @OneToMany(mappedBy = "album")
    private Set<Track> tracks;

    public Album(String title, LocalDate releaseDate, String coverArtPath, String coverArtFileName, String label,
                 int length, Set<Artist> artists, Set<Genre> genres, Set<Track> tracks) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.coverArtPath = coverArtPath;
        this.coverArtFileName = coverArtFileName;
        this.label = label;
        this.length = length;
        this.artists = artists;
        this.genres = genres;
        this.tracks = tracks;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", coverArtPath='" + coverArtPath + '\'' +
                ", coverArtFileName='" + coverArtFileName + '\'' +
                ", label='" + label + '\'' +
                ", length=" + length +
                '}';
    }
}
