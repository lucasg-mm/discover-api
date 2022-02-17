package com.discover.discoverapi.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tracks")
public class Track {
    // PROPERTIES
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "lyrics")
    private String lyrics;

    @Column(name = "length")
    private int length;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "tracks_genres", joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Artist> artists;

    // CONSTRUCTORS
    public Track() {
    }

    public Track(String title, String lyrics, int length, Album album, List<Genre> genres, List<Artist> artists) {
        this.title = title;
        this.lyrics = lyrics;
        this.length = length;
        this.album = album;
        this.genres = genres;
        this.artists = artists;
    }

    // GETTERS/SETTERS
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
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
}
