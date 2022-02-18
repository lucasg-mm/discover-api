package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.repositories.AlbumRepository;
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

public class AlbumServiceTest {
    @InjectMocks
    private AlbumService albumService;

    // mocks the album's repository, which is used by albumService
    @Mock
    private AlbumRepository albumRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Tests the retrieval of an album with an id we already know exists.")
    public void getAlbumByExistingIdTest() {
        // --- GIVEN ---

        // instantiating an album with 1L as id
        final long id = 1L;
        Album existingAlbum = new Album();
        existingAlbum.setId(id);
        Optional<Album> existingAlbumOptional = Optional.of(existingAlbum);

        // the repo returns the correct entity
        doReturn(existingAlbumOptional).when(albumRepository).findById(1L);

        // --- WHEN ---

        Album foundAlbum = albumService.findById(id);

        // --- THEN ---

        assertEquals(existingAlbum.getId(), foundAlbum.getId(),
                "The returned album's id is different than the one being searched." +
                        " Even though the one being searched is also stored.");
    }

    @Test
    @DisplayName("Tests the retrieval of an album with an id we know it does not exist.")
    public void getAlbumByNonExistentId() {
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
    public void findAllAlbumsWhenItsNotEmpty() {
        // --- GIVEN ---

        // sets up two albums
        Album album1 = new Album();
        album1.setId(1L);

        Album album2 = new Album();
        album2.setId(2L);

        // sets up a list composed by the previous two albums
        List<Album> existingAlbums = new ArrayList<>(List.of(album1, album2));

        doReturn(existingAlbums).when(albumRepository).findAll();

        // --- THEN ---

        List<Album> foundAlbums = albumService.findAll();

        // --- WHEN ---

        assertIterableEquals(existingAlbums, foundAlbums,
                "Expected the function albumService.findAll() to return all the existing albums, but it didn't.");
    }

    @Test
    @DisplayName("Tests the creation of a new album.")
    public void createAlbum(){
        // --- GIVEN ---

        // properties for the album that will be created
        final String albumTitle = "The Blueprint";
        final String albumLabel = "Roc-A-Fella Records";

        // sets up the created album returned by the mock repository
        Album expectedCreatedAlbum = new Album();
        expectedCreatedAlbum.setId(1L);
        expectedCreatedAlbum.setTitle(albumTitle);
        expectedCreatedAlbum.setLabel(albumLabel);

        // return it when mocking
        doReturn(expectedCreatedAlbum).when(albumRepository).save(any(Album.class));

        // object passed to the create method
        Album toCreate = new Album();
        toCreate.setTitle(albumTitle);
        toCreate.setLabel(albumLabel);

        // --- THEN ---

        Album actualCreatedAlbum = albumService.create(toCreate);

        // --- WHEN ---

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
        updateInput.setId(id);
        updateInput.setTitle("Flower Boy");
        updateInput.setLabel("Def-Jam Records");
        Optional<Album> oldAlbum = Optional.of(preUpdateAlbum);

        doReturn(oldAlbum).when(albumRepository).findById(1L);
        doReturn(expectedAlbum).when(albumRepository).save(any(Album.class));

        // --- THEN ---

        // call to the update method to get the actual output
        Album actualAlbum = albumService.update(id, updateInput);

        // --- WHEN ---

        assertThat("Method did not return the album the given id, ene though it exists",
                actualAlbum, samePropertyValuesAs(expectedAlbum));
    }

    @Test
    @DisplayName("Tests an attempt of updating an album by id (case when an album with the passed id doesn't exist)")
    public void updateAlbumWithNonExistingId(){
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
            "exist in the database.")
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

}
