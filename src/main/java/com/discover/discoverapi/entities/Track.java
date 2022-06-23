package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Node(labels = {"Track"})
@Schema(description = "Represents a track.")
@Getter @Setter @NoArgsConstructor
public class Track {
    // PROPERTIES
    @Schema(description = "The track's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue
    private long id;

    @Schema(description = "The track's title.")
    @NotEmpty(message = "Track's title should be specified.")
    private String title;

    @Schema(description = "The track's length (in seconds).")
    @Min(value = 1, message = "A track should be at least one second long.")
    @NotNull(message = "The album's track should be specified.")
    private int length;

    @Schema(description = "The album the track is from.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Relationship(type = "CONTAINS", direction = Relationship.Direction.INCOMING)
    private Album album;

    @Schema(description = "The genres the track is classified with.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Relationship(type = "REPRESENTED_BY", direction = Relationship.Direction.INCOMING)
    private Set<Genre> genres;

    @Schema(description = "The track's artists.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Relationship(type = "RECORDS", direction =  Relationship.Direction.INCOMING)
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
