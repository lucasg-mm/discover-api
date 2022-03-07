package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.AlbumRepository;
import com.discover.discoverapi.services.exceptions.FailedToDownloadException;
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

public class AlbumServiceTest {
    @InjectMocks
    private AlbumService albumService;

    // mocks the albumRepository and trackService, which are used by albumService
    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private TrackService trackService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Tests the retrieval of an album with an id we already know exists.")
    public void isFoundIdTheSameAsTheOneBeingSearched() {
        // --- GIVEN ---

        doAnswer(invocation -> {
            // mocking repository response when searching for this user
            Album expectedFoundAlbum = new Album();
            expectedFoundAlbum.setId(invocation.getArgument(0));

            // return the optional
            return Optional.of(expectedFoundAlbum);
        })
                .when(albumRepository)
                .findById(anyLong());

        // --- WHEN ---

        long searchedId = 1L;
        Album foundAlbum = albumService.findById(searchedId);

        // --- THEN ---

        assertEquals(searchedId, foundAlbum.getId(),
                "The returned album's id is different than the one being searched." +
                        " Even though the one being searched is also stored.");
    }

    @Test
    @DisplayName("Tests the retrieval of an album with an id we know it does not exist.")
    public void throwsExceptionWhenSearchesForNonExistingId() {
        // --- GIVEN ---

        Optional<Album> notFoundOptional = Optional.empty();

        // the repo returns an empty optional for every id
        doReturn(notFoundOptional).when(albumRepository).findById(anyLong());

        // -- WHEN ---

        assertThrows(ObjectNotFoundException.class, () -> albumService.findById(1L),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                        "Even though there is no album with the searched id.");

    }

    @Test
    @DisplayName("Tests the retrieval of every album stored.")
    public void isFindingAllAlbums() {
        // --- GIVEN ---

        // sets up two albums
        Album album1 = new Album();
        album1.setId(1L);

        Album album2 = new Album();
        album2.setId(2L);

        // sets up a list composed by the previous two albums
        List<Album> existingAlbums = new ArrayList<>(List.of(album1, album2));

        doReturn(existingAlbums).when(albumRepository).findAll();

        // --- WHEN ---

        List<Album> foundAlbums = albumService.findAll();

        // --- THEN ---

        assertIterableEquals(existingAlbums, foundAlbums,
                "Expected the function albumService.findAll() to return all the existing albums, but it didn't.");
    }

    @Test
    @DisplayName("Tests the creation of a new album.")
    public void createAlbum(){
        // --- GIVEN ---

        // returns whatever what's passed as parameter to the save method
        doAnswer(invocation -> invocation.getArgument(0)).when(albumRepository).save(any(Album.class));

        // properties for the album that will be created
        final String albumTitle = "The Blueprint";
        final String albumLabel = "Roc-A-Fella Records";

        // object passed to the create method
        Album toCreate = new Album();
        toCreate.setTitle(albumTitle);
        toCreate.setLabel(albumLabel);

        // expected created album
        Album expectedCreatedAlbum = new Album();
        expectedCreatedAlbum.setTitle(albumTitle);
        expectedCreatedAlbum.setLabel(albumLabel);

        // --- WHEN ---

        Album actualCreatedAlbum = albumService.create(toCreate);

        // --- THEN ---

        assertEquals(0, actualCreatedAlbum.getId(), "Id of created album should be 0 when creating" +
                " an album.");

        assertThat("The created album doesn't have the same properties" +
                " as the album passed to the albumService.create() function.",
                actualCreatedAlbum, samePropertyValuesAs(expectedCreatedAlbum));
    }

    @Test
    @DisplayName("Tests an attempt of updating an album by id (case when an album with the passed id exists)")
    public void updateAlbumWithExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        final String albumTitle = "IGOR";
        final String labelName = "Columbia Records";
        Album updateInput = new Album();
        updateInput.setTitle(albumTitle);
        updateInput.setLabel(labelName);

        // expected output
        Album expectedAlbum = new Album();
        expectedAlbum.setTitle(albumTitle);
        expectedAlbum.setLabel(labelName);
        expectedAlbum.setId(id);

        // mocking album before the update
        Album preUpdateAlbum = new Album();
        preUpdateAlbum.setId(id);
        preUpdateAlbum.setTitle("Flower Boy");
        preUpdateAlbum.setLabel("Def-Jam Records");
        Optional<Album> oldAlbum = Optional.of(preUpdateAlbum);

        doReturn(oldAlbum).when(albumRepository).findById(1L);
        doAnswer(invocation -> invocation.getArgument(0)).when(albumRepository).save(any(Album.class));

        // --- WHEN ---

        // call to the update method to get the actual output
        Album actualAlbum = albumService.update(id, updateInput);

        // --- THEN ---

        assertThat("Method did not update every property from the album with the given id.",
                actualAlbum, samePropertyValuesAs(expectedAlbum));
    }

    @Test
    @DisplayName("Tests an attempt of updating an album by id (case when an album with the passed id doesn't exist)")
    public void throwsExceptionWhenUpdatesAlbumWithNonExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        final String albumTitle = "IGOR";
        final String labelName = "Columbia Records";
        Album updateInput = new Album();
        updateInput.setTitle(albumTitle);
        updateInput.setLabel(labelName);

        // mocking not finding an album
        doReturn(Optional.empty()).when(albumRepository).findById(anyLong());

        // --- WHEN ---

        // assert the thrown exception
        assertThrows(ObjectNotFoundException.class, () -> albumService.update(id, updateInput),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                        "Even though there is no album with the searched id.");
    }

    @Test
    @DisplayName("Tests if the albumRepository.deleteById() method is being executed once with an id that is supposed to" +
            " exist in the database.")
    public void deleteAlbumWithExistingId(){
        // --- GIVEN ---

        // input to the delete method
        final long id = 1L;

        // mock the delete method
        doReturn(true).when(albumRepository).existsById(id);
        doNothing().when(albumRepository).deleteById(anyLong());

        // --- WHEN ---

        albumService.deleteById(id);

        // --- THEN ---

        verify(albumRepository, times(1).description("albumRepository.deleteById() should be" +
                "executed once with the right id.")).deleteById(id);

    }

    @Test
    @DisplayName("Tests if deleting an album with non-existing id throws an ObjectNotFoundException.")
    public void deleteAlbumWithNonExistingId(){
        // --- GIVEN ---

        // input to the delete method
        final long id = 1L;

        // mock the test
        doReturn(false).when(albumRepository).existsById(id);

        // --- WHEN THEN ---
        assertThrows(ObjectNotFoundException.class, () -> albumService.deleteById(id));
    }

    @Test
    @DisplayName("Tests if the method findAllTracksOfAlbum is returning all tracks of an album")
    public void findAllTracksOfAlbumsReturnAllTracks(){
        // --- GIVEN ---
        // input to the method
        final long id = 1L;

        // define the expected returned tracks
        Track track1 = new Track();
        track1.setTitle("Dark Fantasy");

        Track track2 = new Track();
        track2.setTitle("POWER");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Album object mocked
        Album theAlbum = new Album();
        theAlbum.setTracks(expectedTracks);

        doReturn(Optional.of(theAlbum)).when(albumRepository).findById(id);

        // --- WHEN ---

        Set<Track> actualTracks = albumService.findAllTracksOfAlbum(1);

        // --- THEN ---
        assertIterableEquals(expectedTracks, actualTracks, "findAllTracksOfAlbum is not returning every " +
                "track of the mocked album.");
    }

    @Test
    @DisplayName("Tests if the method deleteTrackFromAlbum is executing the remove method (from the" +
            " tracks set) once and if it's saving the album again after that.")
    public void deleteTrackFromAlbumExecutesARemoveAndSavesAlbum(){
        // --- GIVEN ---

        // define the tracks referenced by the album mock
        Track track1 = new Track();
        track1.setTitle("Dark Fantasy");

        Track track2 = new Track();
        track2.setTitle("POWER");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Album object mocked
        Album theAlbum = new Album();
        theAlbum.setTracks(expectedTracks);

        doReturn(Optional.of(theAlbum)).when(albumRepository).findById(anyLong());

        // define the Track object mocked
        Track theTrackToBeRemoved = track2;

        doReturn(theTrackToBeRemoved).when(trackService).findById(anyLong());

        // --- WHEN ---

        albumService.deleteTrackFromAlbum(1L, 1L);

        // --- THEN ---

        assertFalse(theAlbum.getTracks().contains(theTrackToBeRemoved),
                "The album should not contain the removed track after the execution of deleteTrackFromAlbum");

        verify(albumRepository, times(1)
                .description("The album should be updated after the track removal"))
                .save(theAlbum);
    }

    @Test
    @DisplayName("Tests if the method addTrackToAlbum is executing the add method (from the" +
            " tracks set) once and if it's saving the album again after that.")
    public void addTrackToAlbumExecutesAnAddAndSavesAlbum(){
        // --- GIVEN ---

        // define the tracks referenced by the album mock
        Track track1 = new Track();
        track1.setTitle("Dark Fantasy");

        Track track2 = new Track();
        track2.setTitle("POWER");

        Set<Track> expectedTracks = new HashSet<>(Arrays.asList(track1, track2));

        // define the Album object mocked
        Album theAlbum = new Album();
        theAlbum.setTracks(expectedTracks);

        doReturn(Optional.of(theAlbum)).when(albumRepository).findById(anyLong());

        // define the Track object mocked
        Track theTrackToBeAdded = track2;

        doReturn(theTrackToBeAdded).when(trackService).findById(anyLong());

        // --- WHEN ---

        albumService.addTrackToAlbum(1L, 1L);

        // --- THEN ---

        assertTrue(theAlbum.getTracks().contains(theTrackToBeAdded),
                "The album should contain the added track after the execution of addTrackFromAlbum");

        verify(albumRepository, times(1)
                .description("The album should be updated after the track addition"))
                .save(theAlbum);
    }

    @Test
    @DisplayName("Tests if the method setAlbumCover throws a ObjectNotFoundException exception when provided" +
            " with a non existent album id")
    public void setAlbumCoverThrowsExceptionWhenAlbumDoesNotExist(){
        // ----- GIVEN -----

        AlbumService albumServiceSpy = spy(albumService);

        // findById throws an exception when the album is not found
        doThrow(ObjectNotFoundException.class).when(albumServiceSpy).findById(anyLong());

        // ----- WHEN -----

        assertThrows(ObjectNotFoundException.class, () -> albumServiceSpy.setAlbumCover(1l, null),
                "Expected albumService.setAlbumCover to throw an exception when provided with " +
                        "an id from an album that does not exist.");
    }

    @Test
    @DisplayName("Tests if the method getAlbumCover throws a ObjectNotFoundException exception when provided" +
            " with a non existent album id")
    public void getAlbumCoverThrowsExceptionWhenAlbumDoesNotExist(){
        // ----- GIVEN -----

        AlbumService albumServiceSpy = spy(albumService);

        // the repo returns an empty optional for every id
        doThrow(ObjectNotFoundException.class).when(albumServiceSpy).findById(anyLong());

        // ----- WHEN -----
        assertThrows(ObjectNotFoundException.class, () -> albumServiceSpy.getAlbumCover(1L),
                "Expected albumService.getAlbumCover to throw an exception when provided with " +
                        "an id from an album that does not exist.");
    }

    @Test
    @DisplayName("Tests if the method getAlbumCover throws a FailedToDownloadException exception when the album" +
            " does not have a cover art")
    public void getAlbumCoverThrowsExceptionWhenAlbumDoesNotHaveACoverArt(){
        // ----- GIVEN -----

        AlbumService albumServiceSpy = spy(albumService);

        // mocking the found album (does not have a cover art)
        Album foundAlbum = new Album();
        doReturn(foundAlbum).when(albumServiceSpy).findById(anyLong());

        // ----- WHEN -----

        assertThrows(FailedToDownloadException.class, () -> albumServiceSpy.getAlbumCover(1L), "Expected " +
                "albumService.getAlbumCover to throw an exception when the searched album does not have a cover");
    }
}
