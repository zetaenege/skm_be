package nl.wtrlmn.skm.controllers;

import nl.wtrlmn.skm.dto.MatchOutputDTO;
import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.services.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @GetMapping
    public List<MatchOutputDTO> getAllMatches() {
        return matchService.findAll();
    }

    @GetMapping("/{id}")
    public MatchOutputDTO getMatchById(@PathVariable Long id) {

        return matchService.findByIdDTO(id);
    }

    @PostMapping
    public MatchOutputDTO createMatch(@RequestBody Match match) {

        return matchService.saveFromDTO(match);
    }

    @PutMapping("/{id}")
    public MatchOutputDTO updateMatch(@PathVariable Long id, @RequestBody Match match) {
        match.setId(id);
        return matchService.saveFromDTO(match);
    }

    @DeleteMapping("/{id}")
    public void deleteMatch(@PathVariable Long id) {

        matchService.deleteById(id);
    }

    @GetMapping("/team/{teamId}")
    public  List<MatchOutputDTO> getMatchesByTeam(@PathVariable Long teamId) {
        return matchService.findMatchesByTeam(teamId);
    }
}