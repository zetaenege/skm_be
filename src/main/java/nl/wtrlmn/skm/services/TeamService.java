package nl.wtrlmn.skm.services;

import nl.wtrlmn.skm.dto.TeamInputDTO;
import nl.wtrlmn.skm.dto.TeamOutputDTO;
import nl.wtrlmn.skm.dto.TournamentSimpleDTO;
import nl.wtrlmn.skm.dto.UserOutputDTO;
import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.repository.TeamRepository;
import nl.wtrlmn.skm.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserService userService;

    public List<TeamOutputDTO> findAllAsDTOs() {
        return teamRepository.findAll().stream()
                .map(this::convertToTeamOutputDTO)
                .toList();
    }

    public TeamOutputDTO findByIdDTO(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        return convertToTeamOutputDTO(team);
    }

    public TeamOutputDTO createTeam(TeamInputDTO dto) {
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                .orElseThrow(() -> new RuntimeException("Tournament not found "));
        Team team = new Team();
        team.setName(dto.getName());
        team.setImgProfile(dto.getImgProfile());
        team.setCity(dto.getCity());
        team.setTournament(tournament);
        Team savedTeam = teamRepository.save(team);
        return convertToTeamOutputDTO(savedTeam);
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    public TeamOutputDTO convertToTeamOutputDTO(Team team) {
        TeamOutputDTO dto = new TeamOutputDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setImgProfile(team.getImgProfile());
        dto.setCity(team.getCity());

        if (team.getSquad() != null) {
            List<UserOutputDTO> squadDTOs = team.getSquad().stream().map(user -> {
                UserOutputDTO uDto = userService.convertToUserOutputDTO(user);
                uDto.setMatchesPlayed(0);
                return uDto;
            }).toList();
            dto.setSquad(squadDTOs);
        }


        int played = 0;
        int won = 0;
        int drawn = 0;
        int lost = 0;
        int goalsFor = 0;
        int goalsAgainst = 0;

        if (team.getMatchesHome() != null) {
            for (Match m : team.getMatchesHome()) {
                // Solo contamos si el partido terminó
                if ("FINISHED".equalsIgnoreCase(m.getStatus())) {
                    played++;

                    int hScore = m.getTeamHomeScore();
                    int aScore = m.getTeamAwayScore();

                    goalsFor += hScore;
                    goalsAgainst += aScore;

                    if (hScore > aScore) won++;
                    else if (hScore == aScore) drawn++;
                    else lost++;
                }
            }
        }

        if (team.getMatchesAway() != null) {
            for (Match m : team.getMatchesAway()) {
                if ("FINISHED".equalsIgnoreCase(m.getStatus())) {
                    played++;

                    int hScore = m.getTeamHomeScore();
                    int aScore = m.getTeamAwayScore();

                    goalsFor += aScore;
                    goalsAgainst += hScore;

                    if (aScore > hScore) won++;
                    else if (aScore == hScore) drawn++;
                    else lost++;
                }
            }
        }

        dto.setMatchesPlayed(played);
        dto.setWon(won);
        dto.setDrawn(drawn);
        dto.setLost(lost);
        dto.setGoalsFor(goalsFor);
        dto.setGoalsAgainst(goalsAgainst);
        dto.setGoalDifference(goalsFor - goalsAgainst);
        dto.setPoints((won * 3) + drawn);

        return dto;
    }

    public TeamOutputDTO updateTeamFromDTO(Long id, TeamInputDTO dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));

        if (dto.getName() != null) team.setName(dto.getName());
        if (dto.getImgProfile() != null) team.setImgProfile(dto.getImgProfile());
        if (dto.getCity() != null) team.setCity(dto.getCity());

        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                    .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + dto.getTournamentId()));
            team.setTournament(tournament);
        }
        Team savedTeam = teamRepository.save(team);
        return convertToTeamOutputDTO(savedTeam);
    }
}