package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Genre;
import com.discover.discoverapi.repositories.GenreRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
