package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Album;
import com.discover.discoverapi.repositories.AlbumRepository;
import com.discover.discoverapi.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

public class AlbumServiceTest {
    private AlbumService albumService;

    // mocks the album's repository, which is used by albumService
    @Mock
    private AlbumRepository albumRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        albumService = new AlbumService(albumRepository);
    }

    // tests the retrieval of an album with an id we already
    // know exists
    @Test
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

    // tests the retrieval of an album with an id we know it does not exist
    @Test
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

    // tests the retrieval of every album stored
    @Test
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

    // tests the creation of a new album (without giving an id)
    @Test
    public void createWithoutId(){
        // --- GIVEN ---

        // sets up the expected created album
        Album expectedCreatedAlbum = new Album();
        expectedCreatedAlbum.setId(1L);
        expectedCreatedAlbum.setTitle("The Blueprint");
        expectedCreatedAlbum.setLabel("Roc-A-Fella Records");

        // return it when mocking
        doReturn(expectedCreatedAlbum).when(albumRepository).save(any(Album.class));

        // --- THEN ---

        Album actualCreatedAlbum = albumService.create(expectedCreatedAlbum);

        // --- WHEN ---

        assertEquals(expectedCreatedAlbum, actualCreatedAlbum);
    }
}
