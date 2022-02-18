package com.discover.discoverapi.services;

import com.discover.discoverapi.entities.Artist;
import com.discover.discoverapi.repositories.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.doReturn;

public class ArtistServiceTest {
    @InjectMocks
    private ArtistService artistService;

    @Mock
    private ArtistRepository artistRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Tests the retrieval of an artist with an id we already know exists.")
    public void getAlbumByExistingIdTest() {
        // --- GIVEN ---

        // input for the get by id method
        final long id = 1L;

        // mocking repository response when searching for this user
        Artist expectedFoundArtist = new Artist();
        expectedFoundArtist.setId(id);
        Optional<Artist> existingArtistOptional = Optional.of(expectedFoundArtist);

        // the repo returns the correct entity
        doReturn(existingArtistOptional).when(artistRepository).findById(id);

        // --- WHEN ---

        Artist actualFoundArtist = artistService.findById(id);

        // --- THEN ---

        assertThat("The returned album's id is different than the one being searched." +
                " Even though the one being searched is also stored.",
                actualFoundArtist, samePropertyValuesAs(expectedFoundArtist));
    }
}
