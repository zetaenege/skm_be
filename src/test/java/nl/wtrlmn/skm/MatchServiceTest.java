package nl.wtrlmn.skm.services;

import nl.wtrlmn.skm.dto.MatchOutputDTO;
import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
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

    @Test
    public void testFindAll_Success() {
        Match match1 = new Match(); match1.setId(1L);
        Match match2 = new Match(); match2.setId(2L);
        when(matchRepository.findAll()).thenReturn(Arrays.asList(match1, match2));

        List<MatchOutputDTO> result = matchService.findAll();

        assertEquals(2, result.size());
        verify(matchRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Success() {
        Match match = new Match();
        match.setId(1L);
        match.setStatus("SCHEDULED");
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        MatchOutputDTO result = matchService.findByIdDTO(1L);

        assertNotNull(result);
        assertEquals("SCHEDULED", result.getStatus());
    }

    @Test
    public void testFindById_NotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> matchService.findByIdDTO(99L));
        assertEquals("Match not found with id: 99", exception.getMessage());
    }

    @Test
    public void testFindAllByTournamentId_Success() {
        Match match = new Match(); match.setId(1L);
        when(matchRepository.findByTournamentId(1L)).thenReturn(List.of(match));

        List<MatchOutputDTO> result = matchService.findAllByTournamentId(1L);

        assertEquals(1, result.size());
    }

    @Test
    public void testDeleteById_Success() {
        doNothing().when(matchRepository).deleteById(1L);

        matchService.deleteById(1L);

        verify(matchRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSaveFromDTO_Success() {
        Match match = new Match(); match.setId(1L);
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        MatchOutputDTO result = matchService.saveFromDTO(match);

        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindMatchesByTeam_Success() {
        Match match = new Match(); match.setId(1L);
        when(matchRepository.findByTeamHomeIdOrTeamAwayId(1L, 1L)).thenReturn(List.of(match));

        List<MatchOutputDTO> result = matchService.findMatchesByTeam(1L);

        assertEquals(1, result.size());
    }

    @Test
    public void testUpdateMatchResult_Success() {
        Match match = new Match(); match.setId(1L);
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        MatchOutputDTO result = matchService.updateMatchResult(1L, 2, 1, "FINISHED");

        assertEquals(2, match.getTeamHomeScore());
        assertEquals(1, match.getTeamAwayScore());
        assertEquals("FINISHED", match.getStatus());
        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdateMatchResult_NotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> matchService.updateMatchResult(99L, 0, 0, "FINISHED"));
    }

    @Test
    public void testConvertToOutputDTO_WithFullData() {
        Team home = new Team(); home.setId(1L); home.setName("Home");
        Team away = new Team(); away.setId(2L); away.setName("Away");
        Tournament tournament = new Tournament(); tournament.setId(1L);

        Match match = new Match();
        match.setId(1L);
        match.setTeamHome(home);
        match.setTeamAway(away);
        match.setTournament(tournament);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));

        MatchOutputDTO result = matchService.findByIdDTO(1L);

        assertNotNull(result.getHomeTeam());
        assertNotNull(result.getAwayTeam());
        assertEquals(1L, result.getTournamentId());
    }
}