package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Track;
import com.discover.discoverapi.repositories.TrackRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TrackServiceTest {
    @InjectMocks
    private TrackService trackService;

    @Mock
    private TrackRepository trackRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    private ExecutableValidator executableValidator = Validation
            .buildDefaultValidatorFactory()
            .getValidator()
            .forExecutables();

    @Test
    @DisplayName("Tests the retrieval of a track with an id we already know exists.")
    public void isFoundIdTheSameAsTheOneBeingSearched(){
        // --- GIVEN ---

        // mocks the return of the correct entity
        doAnswer(invocation -> {
            Track expectedFoundTrack = new Track();
            expectedFoundTrack.setId(invocation.getArgument(0));

            // return the optional
            return Optional.of(expectedFoundTrack);
        })
                .when(trackRepository)
                .findById(anyLong());

        // --- WHEN ---

        long searchedId = 1L;
        Track actualFoundTrack = trackService.findById(searchedId);

        // --- THEN ---

        assertEquals(searchedId, actualFoundTrack.getId(),
                "The returned track's id is different than the one being searched." +
                        " Even though the one being searched is also stored.");
    }

    @Test
    @DisplayName("Tests the retrieval of a track with an id we know it does not exist.")
    public void throwsExceptionWhenSearchesForNonExistingId(){
        // --- GIVEN ---

        // the repo returns an empty optional for every id
        doReturn(Optional.empty()).when(trackRepository).findById(anyLong());

        // --- WHEN ---

        // asserts if the exception is thrown
        assertThrows(ObjectNotFoundException.class, () -> trackService.findById(1L),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                        "Even though there is no track with the searched id.");
    }

    @Test
    @DisplayName("Tests the retrieval of every track stored.")
    public void isFindingAllTracks(){
        //  --- GIVEN ---

        // sets up two tracks
        Track track1 = new Track();
        track1.setId(1L);

        Track track2 = new Track();
        track2.setId(2L);

        // sets up a list composed of the previous two tracks
        List<Track> existingTracks = new ArrayList<>(List.of(track1, track2));

        Map<String, Object> response = new HashMap<>();
        response.put("items", existingTracks);
        response.put("totalItems", 2);
        response.put("totalPages", 1);

        doReturn(existingTracks).when(trackRepository).findAll();

        // --- WHEN ---

        Map<String, Object> foundTracks = trackService.findAll(1, 3);

        // --- THEN ---
        assertEquals(response, foundTracks,
                "Expected the function trackService.findAll() to return all the existing tracks, but it didn't.");
    }

    @Test
    @DisplayName("Tests the creation of a new track.")
    public void createTrack(){
        // --- GIVEN ---

        // returns whatever what's passed as parameter to the save method
        doAnswer(invocation -> invocation.getArgument(0)).when(trackRepository).save(any(Track.class));

        // properties for the track that will be created
        final String trackName = "Blame Game";

        // object passed to the create method
        Track toCreate = new Track();
        toCreate.setTitle(trackName);

        // expected created track
        Track expectedCreatedTrack = new Track();
        expectedCreatedTrack.setTitle(trackName);

        // --- WHEN ---

        Track actualCreatedTrack = trackService.create(toCreate);

        // --- THEN ---

        assertEquals(0, actualCreatedTrack.getId(), "Id of created track should be 0 when creating it.");

        assertThat("The created track doesn't have the same properties" +
                        " as the track passed to the trackService.create() function.",
                actualCreatedTrack, samePropertyValuesAs(expectedCreatedTrack));
    }

    @Test
    @DisplayName("Tests an attempt of updating an track by id (case when an track with the passed id exists)")
    public void updateTrackWithExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        final String trackName = "NEW MAGIC WAND";
        Track updateInput = new Track();
        updateInput.setTitle(trackName);

        // expected output
        Track expectedTrack = new Track();
        expectedTrack.setTitle(trackName);
        expectedTrack.setId(id);

        // mocking track before the update
        Track preUpdateTrack = new Track();
        preUpdateTrack.setId(id);
        preUpdateTrack.setTitle("Dark Fantasy");
        Optional<Track> oldTrack = Optional.of(preUpdateTrack);

        doReturn(oldTrack).when(trackRepository).findById(1L);
        doAnswer(invocation -> invocation.getArgument(0)).when(trackRepository).save(any(Track.class));

        // --- WHEN ---

        Track actualTrack = trackService.update(id, updateInput);

        // --- THEN ---

        assertThat("Method did not update every property from the track with the given id.",
                actualTrack, samePropertyValuesAs(expectedTrack));
    }

    @Test
    @DisplayName("Tests an attempt of updating an track by id (case when an track with the passed id does not exist)")
    public void throwsExceptionWhenUpdatesTrackWithNonExistingId(){
        // --- GIVEN ---

        // input to the update method
        final long id = 1L;
        Track updateInput = new Track();

        // mocking not finding a track
        doReturn(Optional.empty()).when(trackRepository).findById(anyLong());

        // --- WHEN ---

        // asserts the thrown exception
        assertThrows(ObjectNotFoundException.class, () -> trackService.update(id, updateInput),
                "Expected findById() to throw an ObjectNotFoundException, but it didn't. " +
                        "Even though there is no track with the searched id.");
    }

    @Test
    @DisplayName("Tests if the trackRepository.deleteById() method is being executed once with an id" +
            " that is supposed to exist in the database.")
    public void deleteTrackWithExistingId(){
        // --- GIVEN ---

        // input to the delete method
        final long id = 1L;

        // mock the delete method
        doReturn(true).when(trackRepository).existsById(id);
        doNothing().when(trackRepository).deleteById(anyLong());

        // --- WHEN ---

        trackService.deleteById(id);

        // --- THEN ---

        verify(trackRepository, times(1).description("trackRepository.deleteById()" +
                " should be called once with the right id.")).deleteById(id);
    }

    @Test
    @DisplayName("Tests if giving incorrect input (empty string, negative page number and size, null values)" +
            " to findByTitleContaining (it should throw ConstraintViolationException).")
    public void findByTitleContainingThrowsConstraintViolationExceptionWhenProvidedWithInvalidInput() throws NoSuchMethodException {
        // --- GIVEN ---

        // invalid input
        final String emptyTitle = "";
        final int negativePageNumber = -3;
        final int zeroPageNumber = 0;
        final int negativePageSize = -3;
        final int zeroPageSize = 0;


        // --- WHEN THEN ---

        Set<ConstraintViolation<Object>> result;

        // executes validation inside the method's parameters (javax annotation)
        result = executableValidator.validateParameters(
                trackService,
                trackService.getClass().getMethod("findByTitleContaining", String.class, int.class, int.class),
                new Object[]{emptyTitle, negativePageNumber, negativePageSize});


        assertEquals(3, result.size(), "Empty title, negative page number and negative page size should " +
                "result in 3 constraint violations.");

        // executes validation inside the method's parameters (javax annotation)
        result = executableValidator.validateParameters(
                trackService,
                trackService.getClass().getMethod("findByTitleContaining", String.class, int.class, int.class),
                new Object[]{emptyTitle, zeroPageNumber, zeroPageSize});


        assertEquals(3, result.size(), "Empty title, zero page number and zero page size should " +
                "result in 3 constraint violations.");
    }
}
