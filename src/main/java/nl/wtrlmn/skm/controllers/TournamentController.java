package nl.wtrlmn.skm.controllers;

import nl.wtrlmn.skm.dto.TournamentInputDTO;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Tournament> getTournamentById(@PathVariable Long id) {
        return tournamentService.findById(id);
    }

    @PostMapping(consumes = "application/json")
    public Tournament createTournament(@RequestBody TournamentInputDTO tournamentInputDTO) {
        return tournamentService.createTournamentFromDTO(tournamentInputDTO);
    }

    @PutMapping("/{id}")
    public Tournament updateTournament(@PathVariable Long id, @RequestBody TournamentInputDTO tournamentInputDTO) {
        return tournamentService.updateTournamentFromDTO(id, tournamentInputDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTournament(@PathVariable Long id) {
        tournamentService.deleteById(id);
    }

    @PostMapping("/{tournamentId}/generate-matches")
    public String generateMatches(@PathVariable Long tournamentId) {
        tournamentService.generateMatchesForTournament(tournamentId);
        return "Matches generated for tournament with id: " + tournamentId;
    }
}