package nl.wtrlmn.skm.services;

import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    // Test 5: Buscar partido por ID
    @Test
    public void testFindById_Success() {
        Match match = new Match();
        match.setId(1L);
        match.setStatus("SCHEDULED");

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        var result = matchService.findByIdDTO(1L);

        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
    }

    // Test 6: Partido no encontrado
    @Test
    public void testFindById_NotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            matchService.findByIdDTO(99L);
        });
    }

    // Test 7: Actualizar resultado - GANA LOCAL
    @Test
    public void testUpdateMatchResult_HomeWins() {
        Match match = new Match();
        match.setId(1L);
        match.setStatus("SCHEDULED");

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // Act: Gana local 2-0
        matchService.updateMatchResult(1L, 2, 0, "FINISHED");

        // Assert: Verificamos que los goles y el status se guardaron bien
        assertEquals(2, match.getTeamHomeScore());
        assertEquals(0, match.getTeamAwayScore());
        assertEquals("FINISHED", match.getStatus());
    }

    // Test 8: Actualizar resultado - EMPATE
    @Test
    public void testUpdateMatchResult_Draw() {
        Match match = new Match();
        match.setId(1L);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // Act: Empatan 1-1
        matchService.updateMatchResult(1L, 1, 1, "FINISHED");

        // Assert: Goles guardados
        assertEquals(1, match.getTeamHomeScore());
        assertEquals(1, match.getTeamAwayScore());
    }

    // Test 9: Actualizar resultado - GANA VISITANTE
    @Test
    public void testUpdateMatchResult_AwayWins() {
        Match match = new Match();
        match.setId(1L);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        // Act: Gana visitante 0-3
        matchService.updateMatchResult(1L, 0, 3, "FINISHED");

        // Assert: Goles guardados
        assertEquals(0, match.getTeamHomeScore());
        assertEquals(3, match.getTeamAwayScore());
    }
}