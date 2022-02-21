package com.discover.discoverapi.services;

import com.discover.discoverapi.repositories.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.doAnswer;

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
    @DisplayName("")
    public void isFoundIdTheSameAsTheOneBeingSearched(){
        // --- GIVEN ---

        // mocks the return of the correct entity
        doAnswer().when(art)

        // --- WHEN ---



        // --- THEN ---
    }
}
