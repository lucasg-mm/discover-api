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

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Schema(description = "Represents an artist.")
@Node(labels = {"Artist"})
@Getter @Setter @NoArgsConstructor
public class Artist{
    // PROPERTIES
    @Schema(description = "The artist's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue
    private Long id;

    @Schema(description = "The artist's unique name.")
    @NotEmpty(message = "Artist's name shouldn't be empty.")
    private String name;

    @Schema(description = "Where the artist's image is located.")
    @JsonIgnore
    private String imagePath;

    @Schema(description = "The name of the artist's image file.")
    @JsonIgnore
    private String imageFileName;

    @Schema(description = "The artist's albums.")
    @JsonIgnore
    @Relationship(type = "RECORDED_BY", direction = Relationship.Direction.INCOMING)
    private Set<Album> albums;

    @Schema(description = "The genres the artist is classified with.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Relationship(type = "REPRESENTED_BY", direction = Relationship.Direction.INCOMING)
    private Set<Genre> genres;

    @Schema(description = "The artist's tracks.")
    @JsonIgnore
    @Relationship(type = "RECORDS", direction =  Relationship.Direction.OUTGOING)
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
