package nl.wtrlmn.skm.controllers;

import nl.wtrlmn.skm.dto.TournamentInputDTO;
import nl.wtrlmn.skm.dto.TournamentOutputDTO;
import nl.wtrlmn.skm.dto.TournamentSimpleDTO;
import nl.wtrlmn.skm.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public List<TournamentSimpleDTO> getAllTournaments() {
        return tournamentService.findAll();
    }

    @GetMapping("/{id}")
    public TournamentOutputDTO getTournamentById(@PathVariable Long id) {

        return tournamentService.findById(id);
    }

    @PostMapping(consumes = "application/json")
    public TournamentSimpleDTO createTournament(@RequestBody TournamentInputDTO tournamentInputDTO) {
        return tournamentService.createTournamentFromDTO(tournamentInputDTO);
    }

    @PutMapping("/{id}")
    public TournamentOutputDTO updateTournament(@PathVariable Long id, @RequestBody TournamentInputDTO tournamentInputDTO) {
        return tournamentService.updateTournamentFromDTO(id, tournamentInputDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTournament(@PathVariable Long id) {
        tournamentService.deleteById(id);
    }

    @PostMapping("/{tournamentId}/generate-matches")
    public ResponseEntity<?> generateMatches(@PathVariable Long tournamentId) {
        try {
            tournamentService.generateMatchesForTournament(tournamentId);

            return ResponseEntity.ok("Matches generated successfully for tournament: " + tournamentId);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/active")
    public List<TournamentOutputDTO> getActiveTournaments() {
        return tournamentService.findAllDTOs().stream()
                .filter(TournamentOutputDTO::isActive)
                .toList();
    }
}