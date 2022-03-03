package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.GenreRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class GenreServiceTest {
    @InjectMocks
    private GenreService genreService;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private AlbumService albumService;

    @Mock
    private ArtistService artistService;

    @Mock
    private TrackService trackService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Tests the retrieval of a genre with an id we already know exists.")
    public void isFoundIdTheSameAsTheOneBeingSearched(){
        // --- GIVEN ---

        // mocks the return of the correct entity
        doAnswer(invocation -> {
            Genre expectedFoundGenre = new Genre();
            expectedFoundGenre.setId(invocation.getArgument(0));

            // return the optional
            return Optional.of(expectedFoundGenre);
        })
                .when(genreRepository)
                .findById(anyLong());

        // --- WHEN ---

        long searchedId = 1L;
        Genre actualFoundGenre = genreService.findById(searchedId);

        // --- THEN ---

        assertEquals(searchedId, actualFoundGenre.getId(),
                "The returned genre's id is different than the one being searched." +
                        " Even though the one being searched is also stored.");
    }

    @Test
    @DisplayName("Tests the retrieval of a genre with an id we know it does not exist.")
    public void throwsExceptionWhenSearchesForNonExistingId(){
        // --- GIVEN ---

        // the repo returns an empty optional for every id
        doReturn(Optional.empty()).when(genreRepository).findById(anyLong());

        // --- WHEN ---

        // asserts if the exception is thrown
        assertThrows(ObjectNotFoundException.class, () -> genreService.findById(1L),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                "Even though there is no genre with the searched id.");
    }

    @Test
    @DisplayName("Tests the retrieval of every genre stored.")
    public void isFindingAllGenres(){
        //  --- GIVEN ---

        // sets up two genres
        Genre genre1 = new Genre();
        genre1.setId(1L);

        Genre genre2 = new Genre();
        genre2.setId(2L);

        // sets up a list composed of the previous two genres
        List<Genre> existingGenres = new ArrayList<>(List.of(genre1, genre2));

        doReturn(existingGenres).when(genreRepository).findAll();

        // --- WHEN ---

        List<Genre> foundGenres = genreService.findAll();

        // --- THEN ---

        assertIterableEquals(existingGenres, foundGenres,
                "Expected the function genreService.findAll() to return all the existing genres, but it didn't.");
    }

    @Test
    @DisplayName("Tests the creation of a new genre.")
    public void createGenre(){
        // --- GIVEN ---

        // returns whatever what's passed as parameter to the save method
        doAnswer(invocation -> invocation.getArgument(0)).when(genreRepository).save(any(Genre.class));

        // properties for the genre that will be created
        final String genreName = "Drill";

        // object passed to the create method
        Genre toCreate = new Genre();
        toCreate.setName(genreName);

        // expected created genre
        Genre expectedCreatedGenre = new Genre();
        expectedCreatedGenre.setName(genreName);

        // --- WHEN ---

        Genre actualCreatedGenre = genreService.create(toCreate);

        // --- THEN ---

        assertEquals(0, actualCreatedGenre.getId(), "Id of created genre should be 0 when creating it.");

        assertThat("The created genre doesn't have the same properties" +
                        " as the genre passed to the genreService.create() function.",
                actualCreatedGenre, samePropertyValuesAs(expectedCreatedGenre));
    }

    @Test
    @DisplayName("Tests an attempt of updating an genre by id (case when an genre with the passed id exists)")
    public void updateGenreWithExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        final String genreName = "Drill";
        Genre updateInput = new Genre();
        updateInput.setName(genreName);

        // expected output
        Genre expectedGenre = new Genre();
        expectedGenre.setName(genreName);
        expectedGenre.setId(id);

        // mocking genre before the update
        Genre preUpdateGenre = new Genre();
        preUpdateGenre.setId(id);
        preUpdateGenre.setName("Gangsta Rap");
        Optional<Genre> oldGenre = Optional.of(preUpdateGenre);

        doReturn(oldGenre).when(genreRepository).findById(1L);
        doAnswer(invocation -> invocation.getArgument(0)).when(genreRepository).save(any(Genre.class));

        // --- WHEN ---

        Genre actualGenre = genreService.update(id, updateInput);

        // --- THEN ---

        assertThat("Method did not update every property from the genre with the given id.",
                actualGenre, samePropertyValuesAs(expectedGenre));
    }

    @Test
    @DisplayName("Tests an attempt of updating an genre by id (case when an genre with the passed id does not exist)")
    public void throwsExceptionWhenUpdatesGenreWithNonExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        Genre updateInput = new Genre();

        // mocking not finding a genre
        doReturn(Optional.empty()).when(genreRepository).findById(anyLong());

        // --- WHEN ---

        // asserts the thrown exception
        assertThrows(ObjectNotFoundException.class, () -> genreService.update(id, updateInput),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                        "Even though there is no genre with the searched id.");
    }

    @Test
    @DisplayName("Tests if the genreRepository.deleteById() method is being executed once with an id" +
            " that is supposed to exist in the database.")
    public void deleteGenreWithExistingId(){
        // --- GIVEN ---

        // input to the delete method
        final long id = 1L;

        // mock the delete method
        doReturn(true).when(genreRepository).existsById(id);
        doNothing().when(genreRepository).deleteById(anyLong());

        // --- WHEN ---

        genreService.deleteById(id);

        // --- THEN ---

        verify(genreRepository, times(1).description("genreRepository.deleteById()" +
                " should be called once with the right id.")).deleteById(id);
    }

    @Test
    @DisplayName("Tests if deleting an genre with non-existing id throws an ObjectNotFoundException")
    public void deleteGenreWithNonExistingIdThrowsException(){
        // --- GIVEN ---

        // input to delete method
        final long id = 1L;

        // mock the test
        doReturn(false).when(genreRepository).existsById(id);

        // --- WHEN THEN ---
        assertThrows(ObjectNotFoundException.class, () -> genreService.deleteById(id));
    }

    @Test
    @DisplayName("Tests if the method findAllAlbumsOfGenre is returning all albums of a genre")
    public void findAllAlbumsOfGenreReturnsAllAlbums(){
        // --- GIVEN ---
        // input to the method
        final long id = 1L;

        // define the expected returned albums
        Album album1 = new Album();
        album1.setTitle("My Beautiful Dark Twisted Fantasy");

        Album album2 = new Album();
        album2.setTitle("Pinata");

        Set<Album> expectedAlbums = new HashSet<>(Arrays.asList(album1, album2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setAlbums(expectedAlbums);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(id);

        // --- WHEN ---

        Set<Album> actualAlbums = genreService.findAllAlbumsOfGenre(1);

        // --- THEN ---
        assertIterableEquals(expectedAlbums, actualAlbums, "findAllAlbumsOfGenre is not returning every " +
                "album of the mocked genre.");
    }

    @Test
    @DisplayName("Tests if the method deleteAlbumFromGenre is executing the remove method (from the" +
            " albums set) once and if it's saving the genre again after that.")
    public void deleteAlbumFromGenreExecutesARemoveAndSavesGenre(){
        // --- GIVEN ---

        // define the albums referenced by the genre mock
        Album album1 = new Album();
        album1.setTitle("Dark Fantasy");

        Album album2 = new Album();
        album2.setTitle("POWER");

        Set<Album> expectedAlbums = new HashSet<>(Arrays.asList(album1, album2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setAlbums(expectedAlbums);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(anyLong());

        // define the Album object mocked
        Album theAlbumToBeRemoved = album2;

        doReturn(theAlbumToBeRemoved).when(albumService).findById(anyLong());

        // --- WHEN ---

        genreService.deleteAlbumFromGenre(1L, 1L);

        // --- THEN ---

        assertFalse(theGenre.getAlbums().contains(theAlbumToBeRemoved),
                "The genre should not contain the removed album after the execution of deleteAlbumFromGenre");

        verify(genreRepository, times(1)
                .description("The genre should be updated after the album removal"))
                .save(theGenre);
    }

    @Test
    @DisplayName("Tests if the method addAlbumToGenre is executing the add method (from the" +
            " albums set) once and if it's saving the genre again after that.")
    public void addAlbumToGenreExecutesAnAddAndSavesGenre(){
        // --- GIVEN ---

        // define the albums referenced by the genre mock
        Album album1 = new Album();
        album1.setTitle("Dark Fantasy");

        Album album2 = new Album();
        album2.setTitle("POWER");

        Set<Album> expectedAlbums = new HashSet<>(Arrays.asList(album1, album2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setAlbums(expectedAlbums);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(anyLong());

        // define the Album object mocked
        Album theAlbumToBeAdded = album2;

        doReturn(theAlbumToBeAdded).when(albumService).findById(anyLong());

        // --- WHEN ---

        genreService.addAlbumToGenre(1L, 1L);

        // --- THEN ---

        assertTrue(theGenre.getAlbums().contains(theAlbumToBeAdded),
                "The genre should contain the added album after the execution of deleteAlbumFromGenre");

        verify(genreRepository, times(1)
                .description("The genre should be updated after the album removal"))
                .save(theGenre);
    }

    @Test
    @DisplayName("Tests if the method findAllArtistsOfGenre is returning all artists of a genre")
    public void findAllArtistsOfGenreReturnsAllArtists(){
        // --- GIVEN ---
        // input to the method
        final long id = 1L;

        // define the expected returned artists
        Artist artist1 = new Artist();
        artist1.setName("Pusha T");

        Artist artist2 = new Artist();
        artist2.setName("Nina Simone");

        Set<Artist> expectedArtists = new HashSet<>(Arrays.asList(artist1, artist2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setArtists(expectedArtists);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(id);

        // --- WHEN ---

        Set<Artist> actualArtists = genreService.findAllArtistsOfGenre(1);

        // --- THEN ---
        assertIterableEquals(expectedArtists, actualArtists, "findAllArtistsOfGenre is not returning every " +
                "artist of the mocked genre.");
    }

    @Test
    @DisplayName("Tests if the method deleteArtistFromGenre is executing the remove method (from the" +
            " artists set) once and if it's saving the genre again after that.")
    public void deleteArtistFromGenreExecutesARemoveAndSavesGenre(){
        // --- GIVEN ---

        // define the artists referenced by the genre mock
        Artist artist1 = new Artist();
        artist1.setName("Nina Simone");

        Artist artist2 = new Artist();
        artist2.setName("Pusha T");

        Set<Artist> expectedArtists = new HashSet<>(Arrays.asList(artist1, artist2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setArtists(expectedArtists);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(anyLong());

        // define the Artist object mocked
        Artist theArtistToBeRemoved = artist2;

        doReturn(theArtistToBeRemoved).when(artistService).findById(anyLong());

        // --- WHEN ---

        genreService.deleteArtistFromGenre(1L, 1L);

        // --- THEN ---

        assertFalse(theGenre.getArtists().contains(theArtistToBeRemoved),
                "The genre should not contain the removed artist after the execution of deleteArtistFromGenre");

        verify(genreRepository, times(1)
                .description("The genre should be updated after the artist removal"))
                .save(theGenre);
    }

    @Test
    @DisplayName("Tests if the method addArtistToGenre is executing the add method (from the" +
            " artists set) once and if it's saving the genre again after that.")
    public void addArtistToGenreExecutesAnAddAndSavesGenre(){
        // --- GIVEN ---

        // define the artists referenced by the genre mock
        Artist artist1 = new Artist();
        artist1.setName("Kendrick Lamar");

        Artist artist2 = new Artist();
        artist2.setName("Kid Cudi");

        Set<Artist> expectedArtists = new HashSet<>(Arrays.asList(artist1, artist2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setArtists(expectedArtists);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(anyLong());

        // define the Artist object mocked
        Artist theArtistToBeAdded = artist2;

        doReturn(theArtistToBeAdded).when(artistService).findById(anyLong());

        // --- WHEN ---

        genreService.addArtistToGenre(1L, 1L);

        // --- THEN ---

        assertTrue(theGenre.getArtists().contains(theArtistToBeAdded),
                "The genre should contain the added artist after the execution of addArtistFromGenre");

        verify(genreRepository, times(1)
                .description("The genre should be updated after the artist addition"))
                .save(theGenre);
    }

    @Test
    @DisplayName("Tests if the method findAllTracksOfGenre is returning all tracks of a genre")
    public void findAllTracksOfGenreReturnsAllTracks(){
        // --- GIVEN ---
        // input to the method
        final long id = 1L;

        // define the expected returned tracks
        Track track1 = new Track();
        track1.setTitle("Who Dat Boy");

        Track track2 = new Track();
        track2.setTitle("Off the Grid");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setTracks(expectedTracks);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(id);

        // --- WHEN ---

        Set<Track> actualTracks = genreService.findAllTracksOfGenre(1);

        // --- THEN ---
        assertIterableEquals(expectedTracks, actualTracks, "findAllTracksOfGenre is not returning every " +
                "track of the mocked genre.");
    }

    @Test
    @DisplayName("Tests if the method deleteTrackFromGenre is executing the remove method (from the" +
            " tracks set) once and if it's saving the genre again after that.")
    public void deleteTrackFromGenreExecutesARemoveAndSavesGenre(){
        // --- GIVEN ---

        // define the tracks referenced by the genre mock
        Track track1 = new Track();
        track1.setTitle("Who Dat Boy");

        Track track2 = new Track();
        track2.setTitle("Off The Grid");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setTracks(expectedTracks);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(anyLong());

        // define the Track object mocked
        Track theTrackToBeRemoved = track2;

        doReturn(theTrackToBeRemoved).when(trackService).findById(anyLong());

        // --- WHEN ---

        genreService.deleteTrackFromGenre(1L, 1L);

        // --- THEN ---

        assertFalse(theGenre.getTracks().contains(theTrackToBeRemoved),
                "The genre should not contain the removed track after the execution of deleteTrackFromGenre");

        verify(genreRepository, times(1)
                .description("The genre should be updated after the track removal"))
                .save(theGenre);
    }

    @Test
    @DisplayName("Tests if the method addTrackToGenre is executing the add method (from the" +
            " tracks set) once and if it's saving the genre again after that.")
    public void addTrackToGenreExecutesAnAddAndSavesGenre(){
        // --- GIVEN ---

        // define the tracks referenced by the genre mock
        Track track1 = new Track();
        track1.setTitle("Gasoline");

        Track track2 = new Track();
        track2.setTitle("Out of Time");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Genre object mocked
        Genre theGenre = new Genre();
        theGenre.setTracks(expectedTracks);

        doReturn(Optional.of(theGenre)).when(genreRepository).findById(anyLong());

        // define the Track object mocked
        Track theTrackToBeAdded = track2;

        doReturn(theTrackToBeAdded).when(trackService).findById(anyLong());

        // --- WHEN ---

        genreService.addTrackToGenre(1L, 1L);

        // --- THEN ---

        assertTrue(theGenre.getTracks().contains(theTrackToBeAdded),
                "The genre should contain the added track after the execution of addTrackFromGenre");

        verify(genreRepository, times(1)
                .description("The genre should be updated after the track addition"))
                .save(theGenre);
    }
}
