package nl.wtrlmn.skm.controllers;

import nl.wtrlmn.skm.dto.TeamInputDTO;
import nl.wtrlmn.skm.dto.TeamOutputDTO;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<TeamOutputDTO> getAllTeams() {
        return teamService.findAllAsDTOs();
    }

    @GetMapping("/{id}")
    public TeamOutputDTO getTeamById(@PathVariable Long id) {
        return teamService.findById(id)
                .map(teamService::convertToTeamOutputDTO)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + id));
    }

    @PostMapping
    public TeamOutputDTO createTeam(@RequestBody TeamInputDTO teamInputDTO) {
        return teamService.convertToTeamOutputDTO(
                teamService.createTeamFromDTO(teamInputDTO)
        );
    }

    @PutMapping("/{id}")
    public TeamOutputDTO updateTeam(@PathVariable Long id, @RequestBody TeamInputDTO teamInputDTO) {
        return teamService.convertToTeamOutputDTO(
                teamService.updateTeamFromDTO(id, teamInputDTO)
        );
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }
}