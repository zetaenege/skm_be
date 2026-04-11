package nl.wtrlmn.skm;

import nl.wtrlmn.skm.dto.TournamentInputDTO;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.repository.TournamentRepository;
import nl.wtrlmn.skm.services.TournamentService;
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
public class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentService tournamentService;

    @Test
    public void testFindById_Success() {
        Tournament tournament = new Tournament();
        tournament.setId(1L);
        tournament.setName("Waterlemon League");

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        var result = tournamentService.findById(1L);

        assertNotNull(result);
        assertEquals("Waterlemon League", result.getName());
    }

    @Test
    public void testFindById_NotFound() {
        when(tournamentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            tournamentService.findById(99L);
        });
    }

    @Test
    public void testCreateTournament_Success() {
        TournamentInputDTO dto = new TournamentInputDTO();
        dto.setName("Nueva Copa");

        Tournament savedTournament = new Tournament();
        savedTournament.setId(2L);
        savedTournament.setName("Nueva Copa");

        when(tournamentRepository.save(any(Tournament.class))).thenReturn(savedTournament);

        var result = tournamentService.createTournamentFromDTO(dto);

        assertNotNull(result);
        assertEquals("Nueva Copa", result.getName());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(tournamentRepository).deleteById(1L);

        tournamentService.deleteById(1L);

        verify(tournamentRepository, times(1)).deleteById(1L);
    }
}