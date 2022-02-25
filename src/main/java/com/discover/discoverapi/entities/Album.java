package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "albums")
@Getter @Setter @NoArgsConstructor
@JsonIgnoreProperties({ "artists", "tracks", "genres" })
public class Album {

    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "release_date")
    private Timestamp releaseDate;

    @Column(name = "cover_art_url")
    private String coverArtUrl;

    @Column(name = "label")
    private String label;

    @Column(name = "length")
    private int length;

    @ManyToMany
    @JoinTable(name = "artists_albums", joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> artists;

    @ManyToMany
    @JoinTable(name = "albums_genres", joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @OneToMany(mappedBy = "album")
    @JsonManagedReference
    private Set<Track> tracks;

    // CONSTRUCTORS
    public Album(String title, Timestamp releaseDate, String coverArtUrl,
                 String label, int length, Set<Artist> artists, Set<Genre> genres, Set<Track> tracks) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.coverArtUrl = coverArtUrl;
        this.label = label;
        this.length = length;
        this.artists = artists;
        this.genres = genres;
        this.tracks = tracks;
    }

    // METHODS
    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", coverArtUrl='" + coverArtUrl + '\'' +
                ", label='" + label + '\'' +
                ", length=" + length +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        if (length != album.length) return false;
        if (!title.equals(album.title)) return false;
        if (!releaseDate.equals(album.releaseDate)) return false;
        return label.equals(album.label);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + releaseDate.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + length;
        return result;
    }
}
