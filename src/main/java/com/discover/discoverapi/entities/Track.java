package com.discover.discoverapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tracks")
@Getter @Setter @NoArgsConstructor
@JsonIgnoreProperties({"album", "genres", "artists"})
public class Track {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "lyrics")
    private String lyrics;

    @Column(name = "length")
    private int length;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonBackReference
    private Album album;

    @ManyToMany
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable
    private Set<Artist> artists;

    // CONSTRUCTORS

    public Track(String title, String lyrics, int length, Album album, Set<Genre> genres, Set<Artist> artists) {
        this.title = title;
        this.lyrics = lyrics;
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
                ", lyrics='" + lyrics + '\'' +
                ", length=" + length +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Track track = (Track) o;

        if (length != track.length) return false;
        if (!title.equals(track.title)) return false;
        return album.equals(track.album);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + length;
        result = 31 * result + album.hashCode();
        return result;
    }
}
