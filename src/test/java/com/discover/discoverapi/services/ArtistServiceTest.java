package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.ArtistRepository;
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

public class ArtistServiceTest {
    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private TrackService trackService;

    @Mock
    private AlbumService albumService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Tests the retrieval of an artist with an id we already know exists.")
    public void isFoundIdTheSameAsTheOneBeingSearched() {
        // --- GIVEN ---

        // the repo returns the correct entity
        doAnswer(invocation -> {
            // mocking repository response when searching for this user
            Artist expectedFoundArtist = new Artist();
            expectedFoundArtist.setId(invocation.getArgument(0));

            // return the optional
            return Optional.of(expectedFoundArtist);
        })
                .when(artistRepository)
                .findById(anyLong());

        // --- WHEN ---

        long searchedId = 1L;
        Artist actualFoundArtist = artistService.findById(searchedId);

        // --- THEN ---

        assertEquals(searchedId, actualFoundArtist.getId(),
                "The returned artist's id is different than the one being searched." +
                        " Even though the one being searched is also stored.");
    }

    @Test
    @DisplayName("Tests the retrieval of an artist with an id we know it does not exist.")
    public void throwsExceptionWhenSearchesForNonExistingId() {
        // --- GIVEN ---

        // the repo returns an empty optional for every id
        doReturn(Optional.empty()).when(artistRepository).findById(anyLong());

        // -- WHEN ---

        assertThrows(ObjectNotFoundException.class, () -> artistService.findById(1L),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                        "Even though there is no artist with the searched id.");
    }

    @Test
    @DisplayName("Tests the retrieval of every artist stored")
    public void isFindingAllArtists(){
        // --- GIVEN ---

        // sets up two artists
        Artist artist1 = new Artist();
        artist1.setId(1L);

        Artist artist2 = new Artist();
        artist2.setId(2L);

        // sets up a list composed by the previous two artists
        List<Artist> existingArtists = new ArrayList<>(List.of(artist1, artist2));

        doReturn(existingArtists).when(artistRepository).findAll();

        // --- WHEN ---

        List<Artist> foundArtists = artistService.findAll();

        // --- THEN ---

        assertIterableEquals(existingArtists, foundArtists,
                "Expected the function artistService.findAll() to return all the existing artists, but it didn't.");
    }

    @Test
    @DisplayName("Tests the creation of a new artist.")
    public void createArtist(){
        // --- GIVEN ---

        // returns whatever what's passed as parameter to the save method
        doAnswer(invocation -> invocation.getArgument(0)).when(artistRepository).save(any(Artist.class));

        // properties for the artists that will be created
        final String artistName = "The Notorious B.I.G.";
        final String artistImageURL = "https://upload.wikimedia.org/wikipedia/pt/5/51/The_Notorious_B.I.G.jpg";

        // object passed to the create method
        Artist toCreate = new Artist();
        toCreate.setName(artistName);
        toCreate.setImageURL(artistImageURL);

        // expected created artist
        Artist expectedCreatedArtist = new Artist();
        expectedCreatedArtist.setName(artistName);
        expectedCreatedArtist.setImageURL(artistImageURL);

        // --- WHEN ---

        Artist actualCreatedArtist = artistService.create(toCreate);

        // --- THEN ---

        assertEquals(0, actualCreatedArtist.getId(), "Id of created artist should be 0 when creating it.");

        assertThat("The created artist doesn't have the same properties" +
                " as the artist passed to the artistService.create() function.",
                actualCreatedArtist, samePropertyValuesAs(expectedCreatedArtist));
    }

    @Test
    @DisplayName("Tests an attempt of updating an artist by id (case when an artist with the passed id exists)")
    public void updateArtistWithExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        final String artistName = "The Notorious B.I.G.";
        final String artistImageURL = "https://upload.wikimedia.org/wikipedia/pt/5/51/The_Notorious_B.I.G.jpg";
        Artist updateInput = new Artist();
        updateInput.setName(artistName);
        updateInput.setImageURL(artistImageURL);

        // expected output
        Artist expectedArtist = new Artist();
        expectedArtist.setName(artistName);
        expectedArtist.setId(id);
        expectedArtist.setImageURL(artistImageURL);

        // mocking artist before the update
        Artist preUpdateArtist = new Artist();
        preUpdateArtist.setId(id);
        preUpdateArtist.setName("Tyler, the Creator");
        preUpdateArtist.setImageURL("https://lastfm.freetls.fastly.net/i/u/avatar170s/faa68f71f3b2a48ca89228c2c2aa72d3");
        Optional<Artist> oldArtist = Optional.of(preUpdateArtist);

        doReturn(oldArtist).when(artistRepository).findById(1L);
        doAnswer(invocation -> invocation.getArgument(0)).when(artistRepository).save(any(Artist.class));

        // --- WHEN ---

        Artist actualArtist = artistService.update(id, updateInput);

        // --- THEN ---

        assertThat("Method did not update every property from the artist with the given id.",
                actualArtist, samePropertyValuesAs(expectedArtist));
    }

    @Test
    @DisplayName("Tests an attempt of updating an artist by id (case when an artist with the passed id does not exist)")
    public void throwsExceptionWhenUpdatesArtistWithNonExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        Artist updateInput = new Artist();

        // mocking not finding an artist
        doReturn(Optional.empty()).when(artistRepository).findById(anyLong());

        // --- WHEN ---

        // asserts the thrown exception
        assertThrows(ObjectNotFoundException.class, () -> artistService.update(id, updateInput),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                        "Even though there is no artist with the searched id.");
    }

    @Test
    @DisplayName("Tests if the artistRepository.deleteById() method is being executed once with an id" +
            " that is supposed to exist in the database.")
    public void deleteArtistWithExistingId(){
        // --- GIVEN ---

        // input to the delete method
        final long id = 1L;

        // mock the delete method
        doReturn(true).when(artistRepository).existsById(id);
        doNothing().when(artistRepository).deleteById(anyLong());

        // --- WHEN ---

        artistService.deleteById(id);

        // --- THEN ---

        verify(artistRepository, times(1).description("artistRepository.deleteById()" +
                " should be called once with the right id.")).deleteById(id);
    }

    @Test
    @DisplayName("Tests if deleting an artist with non-existing id throws an ObjectNotFoundException")
    public void deleteArtistWithNonExistingIdThrowsException(){
        // --- GIVEN ---

        // input to delete method
        final long id = 1L;

        // mock the test
        doReturn(false).when(artistRepository).existsById(id);

        // --- WHEN THEN ---
        assertThrows(ObjectNotFoundException.class, () -> artistService.deleteById(id));
    }

    @Test
    @DisplayName("Tests if the method findAllTracksOfArtist is returning all tracks of an artist")
    public void findAllTracksOfArtistsReturnAllTracks(){
        // --- GIVEN ---
        // input to the method
        final long id = 1L;

        // define the expected returned tracks
        Track track1 = new Track();
        track1.setTitle("Dark Fantasy");

        Track track2 = new Track();
        track2.setTitle("POWER");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Artist object mocked
        Artist theArtist = new Artist();
        theArtist.setTracks(expectedTracks);

        doReturn(Optional.of(theArtist)).when(artistRepository).findById(id);

        // --- WHEN ---

        Set<Track> actualTracks = artistService.findAllTracksOfArtist(1);

        // --- THEN ---
        assertIterableEquals(expectedTracks, actualTracks, "findAllTracksOfArtist is not returning every " +
                "track of the mocked artist.");
    }

    @Test
    @DisplayName("Tests if the method deleteTrackFromArtist is executing the remove method (from the" +
            " tracks set) once and if it's saving the artist again after that.")
    public void deleteTrackFromArtistExecutesARemoveAndSavesArtist(){
        // --- GIVEN ---

        // define the tracks referenced by the artist mock
        Track track1 = new Track();
        track1.setTitle("Dark Fantasy");

        Track track2 = new Track();
        track2.setTitle("POWER");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Artist object mocked
        Artist theArtist = new Artist();
        theArtist.setTracks(expectedTracks);

        doReturn(Optional.of(theArtist)).when(artistRepository).findById(anyLong());

        // define the Track object mocked
        Track theTrackToBeRemoved = track2;

        doReturn(theTrackToBeRemoved).when(trackService).findById(anyLong());

        // --- WHEN ---

        artistService.deleteTrackFromArtist(1L, 1L);

        // --- THEN ---

        assertFalse(theArtist.getTracks().contains(theTrackToBeRemoved),
                "The artist should not contain the removed track after the execution of deleteTrackFromArtist");

        verify(artistRepository, times(1)
                .description("The artist should be updated after the track removal"))
                .save(theArtist);
    }

    @Test
    @DisplayName("Tests if the method addTrackToArtist is executing the add method (from the" +
            " tracks set) once and if it's saving the artist again after that.")
    public void addTrackToArtistExecutesAnAddAndSavesArtist(){
        // --- GIVEN ---

        // define the tracks referenced by the artist mock
        Track track1 = new Track();
        track1.setTitle("Dark Fantasy");

        Track track2 = new Track();
        track2.setTitle("POWER");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Artist object mocked
        Artist theArtist = new Artist();
        theArtist.setTracks(expectedTracks);

        doReturn(Optional.of(theArtist)).when(artistRepository).findById(anyLong());

        // define the Track object mocked
        Track theTrackToBeAdded = track2;

        doReturn(theTrackToBeAdded).when(trackService).findById(anyLong());

        // --- WHEN ---

        artistService.addTrackToArtist(1L, 1L);

        // --- THEN ---

        assertTrue(theArtist.getTracks().contains(theTrackToBeAdded),
                "The artist should contain the added track after the execution of addTrackFromArtist");

        verify(artistRepository, times(1)
                .description("The artist should be updated after the track addition"))
                .save(theArtist);
    }

    @Test
    @DisplayName("Tests if the method findAllAlbumsOfArtist is returning all albums of an artist")
    public void findAllAlbumsOfArtistsReturnAllAlbums(){
        // --- GIVEN ---
        // input to the method
        final long id = 1L;

        // define the expected returned albums
        Album album1 = new Album();
        album1.setTitle("Dark Fantasy");

        Album album2 = new Album();
        album2.setTitle("POWER");

        Set<Album> expectedAlbums = new HashSet<>(Arrays.asList(album1, album2));

        // define the Artist object mocked
        Artist theArtist = new Artist();
        theArtist.setAlbums(expectedAlbums);

        doReturn(Optional.of(theArtist)).when(artistRepository).findById(id);

        // --- WHEN ---

        Set<Album> actualAlbums = artistService.findAllAlbumsOfArtist(1);

        // --- THEN ---
        assertIterableEquals(expectedAlbums, actualAlbums, "findAllAlbumsOfArtist is not returning every " +
                "album of the mocked artist.");
    }

    @Test
    @DisplayName("Tests if the method deleteAlbumFromArtist is executing the remove method (from the" +
            " albums set) once and if it's saving the artist again after that.")
    public void deleteAlbumFromArtistExecutesARemoveAndSavesArtist(){
        // --- GIVEN ---

        // define the albums referenced by the artist mock
        Album album1 = new Album();
        album1.setTitle("Dark Fantasy");

        Album album2 = new Album();
        album2.setTitle("POWER");

        Set<Album> expectedAlbums = new HashSet<>(Arrays.asList(album1, album2));

        // define the Artist object mocked
        Artist theArtist = new Artist();
        theArtist.setAlbums(expectedAlbums);

        doReturn(Optional.of(theArtist)).when(artistRepository).findById(anyLong());

        // define the Album object mocked
        Album theAlbumToBeRemoved = album2;

        doReturn(theAlbumToBeRemoved).when(albumService).findById(anyLong());

        // --- WHEN ---

        artistService.deleteAlbumFromArtist(1L, 1L);

        // --- THEN ---

        assertFalse(theArtist.getAlbums().contains(theAlbumToBeRemoved),
                "The artist should not contain the removed album after the execution of deleteAlbumFromArtist");

        verify(artistRepository, times(1)
                .description("The artist should be updated after the album removal"))
                .save(theArtist);
    }

    @Test
    @DisplayName("Tests if the method addAlbumToArtist is executing the add method (from the" +
            " albums set) once and if it's saving the artist again after that.")
    public void addAlbumToArtistExecutesAnAddAndSavesArtist(){
        // --- GIVEN ---

        // define the albums referenced by the artist mock
        Album album1 = new Album();
        album1.setTitle("Dark Fantasy");

        Album album2 = new Album();
        album2.setTitle("POWER");

        Set<Album> expectedAlbums = new HashSet<>(Arrays.asList(album1, album2));

        // define the Artist object mocked
        Artist theArtist = new Artist();
        theArtist.setAlbums(expectedAlbums);

        doReturn(Optional.of(theArtist)).when(artistRepository).findById(anyLong());

        // define the Album object mocked
        Album theAlbumToBeAdded = album2;

        doReturn(theAlbumToBeAdded).when(albumService).findById(anyLong());

        // --- WHEN ---

        artistService.addAlbumToArtist(1L, 1L);

        // --- THEN ---

        assertTrue(theArtist.getAlbums().contains(theAlbumToBeAdded),
                "The artist should contain the added album after the execution of addAlbumFromArtist");

        verify(artistRepository, times(1)
                .description("The artist should be updated after the album addition"))
                .save(theArtist);
    }
}
