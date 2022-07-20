package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import javax.validation.constraints.NotEmpty;

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

    // CONSTRUCTORS
    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // METHODS
    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
