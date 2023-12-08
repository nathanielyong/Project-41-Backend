package com.snowtheghost.project41.api.controllers;

import com.snowtheghost.project41.api.models.responses.games.GetGameResponse;
import com.snowtheghost.project41.api.models.responses.games.GetGamesResponse;
import com.snowtheghost.project41.services.AuthenticationService;
import com.snowtheghost.project41.services.GameService;
import com.snowtheghost.project41.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// class GameControllerTest {

//     @Mock
//     private GameService gameService;

//     @InjectMocks
//     private GameController gameController;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testGetGames_Success() {
//         when(gameService.getGames("researcherId")).thenReturn(GetGamesResponse("GameID", "Type", "State")));
//         ResponseEntity<GetGamesResponse> responseEntity = gameController.getGames("researcherId");

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

//         verify(gameService).getGames("researcherId");
//         verifyNoMoreInteractions(gameService);
//     }
// }
