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

@Schema(description = "Represents a user")
@Node(labels = {"AppUser"})
@Getter @Setter @NoArgsConstructor
public class AppUser {
    // PROPERTIES
    @Schema(description = "The user's unique identifier.")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue
    private Long id;

    @Schema(description = "The user's username")
    @NotEmpty(message = "Username shouldn't be empty")
    private String username;

    @Schema(description = "The user's password")
    @NotEmpty(message = "The user's password shouldn't be empty")
    private String password;

    @Schema(description = "The user's role in the app, for authorization purposes")
    private String role;

    @Schema(description = "The list of albums liked by the user")
    @JsonIgnore
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private Set<Album> likedAlbums;

    @Schema(description = "The list of tracks liked by the user")
    @JsonIgnore
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private Set<Track> likedTracks;

    @Schema(description = "The list of genres liked by the user")
    @JsonIgnore
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private Set<Genre> likedGenres;

    @Schema(description = "The list of artists liked by the user")
    @JsonIgnore
    @Relationship(type = "LIKES", direction = Relationship.Direction.OUTGOING)
    private Set<Artist> likedArtists;

    // CONSTRUCTORS
    public AppUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public AppUser(String username, String password, String role, Set<Album> likedAlbums, Set<Track> likedTracks,
                   Set<Genre> likedGenres, Set<Artist> likedArtists) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.likedAlbums = likedAlbums;
        this.likedTracks = likedTracks;
        this.likedGenres = likedGenres;
        this.likedArtists = likedArtists;
    }

    // METHODS
    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
