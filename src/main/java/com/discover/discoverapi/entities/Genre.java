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

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Schema(description = "Represents a genre.")
@Node(labels = {"Genre"})
@Getter @Setter @NoArgsConstructor
public class Genre {
    // PROPERTIES
    @Schema(description = "The genre's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue
    private long id;

    @Schema(description = "The genre's unique name.")
    @NotEmpty(message = "Genre's name shouldn't be empty.")
    private String name;

    @Schema(description = "The albums classified with the genre.")
    @JsonIgnore
    @Relationship(type = "REPRESENTED_BY", direction = Relationship.Direction.OUTGOING)
    private Set<Album> albums;

    @Schema(description = "The artists classified with the genre.")
    @JsonIgnore
    @Relationship(type = "REPRESENTED_BY", direction = Relationship.Direction.OUTGOING)
    private Set<Artist> artists;

    @Schema(description = "The tracks classified with the genre.")
    @JsonIgnore
    @Relationship(type = "REPRESENTED_BY", direction = Relationship.Direction.OUTGOING)
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
