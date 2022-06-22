package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Represents an album.")
@Node(labels = {"Album"})
@Getter @Setter @NoArgsConstructor
public class Album {

    // PROPERTIES
    @Schema(description = "The album's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue
    private long id;

    @Schema(description = "The album's title.")
    @NotEmpty(message = "The album's title should be specified.")
    private String title;

    @Schema(type="string", description = "The album's release date (yyyy-MM-dd format).", example = "1999-08-26")
    @NotNull
    private LocalDate releaseDate;

    @Schema(description = "The album's cover art path.")
    @JsonIgnore
    private String coverArtPath;

    @Schema(description = "The album's cover art file name.")
    @JsonIgnore
    private String coverArtFileName;

    @Schema(description = "The album's label.")
    private String label;

    @Schema(description = "The album's length (in seconds).")
    @NotNull(message = "The album's length should be specified.")
    @Min(value = 1, message = "An album should be at least one second long.")
    private int length;

    @Schema(description = "The album's artists.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Relationship(type = "RECORDED_BY", direction = Relationship.Direction.OUTGOING)
    private Set<Artist> artists;

    @Schema(description = "The genres the album is classified with.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Relationship(type = "CLASSIFIED_AS", direction = Relationship.Direction.OUTGOING)
    private Set<Genre> genres;

    @Schema(description = "The album's tracks.")
    @JsonIgnore
    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
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
