package nl.wtrlmn.skm.controllers;

import nl.wtrlmn.skm.dto.TeamInputDTO;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Team> getTeamById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @PostMapping
    public Team createTeam(@RequestBody TeamInputDTO teamInputDTO) {
        return teamService.createTeamFromDTO(teamInputDTO);
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable Long id, @RequestBody TeamInputDTO teamInputDTO) {
        return teamService.updateTeamFromDTO(id, teamInputDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteById(id);
    }
}