package nl.wtrlmn.skm.services;

import nl.wtrlmn.skm.dto.TeamInputDTO;
import nl.wtrlmn.skm.dto.TeamOutputDTO;
import nl.wtrlmn.skm.dto.TournamentSimpleDTO;
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




    public List<TeamOutputDTO> findAllAsDTOs() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream()
                .map(this::convertToTeamOutputDTO)
                .toList();
    }

    public Optional<Team> findById(Long id) {
        return teamRepository.findById(id);
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

        Tournament tournament = team.getTournament();
        TournamentSimpleDTO tournamentDTO = new TournamentSimpleDTO();
        tournamentDTO.setId(tournament.getId());
        tournamentDTO.setName(tournament.getName());
        tournamentDTO.setImgProfile(tournament.getImgProfile());
        tournamentDTO.setStartDate(tournament.getStartDate());
        tournamentDTO.setEndDate(tournament.getEndDate());

        dto.setTournament(tournamentDTO);

        return dto;
    }


    public Team createTeamFromDTO(TeamInputDTO dto) {
        Tournament tournament = tournamentRepository.findById(dto.getTournamentId()).orElseThrow(() -> new IllegalArgumentException("Tournament not found with id: " + dto.getTournamentId()));
        Team team = new Team();
        team.setName(dto.getName());
        team.setImgProfile(dto.getImgProfile());
        team.setTournament(tournament);
        team.setCity(dto.getCity());
        return teamRepository.save(team);
    }

    public Team updateTeamFromDTO(Long id, TeamInputDTO dto) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + id));

        Tournament tournament = tournamentRepository.findById(dto.getTournamentId()).orElseThrow(() -> new IllegalArgumentException("Tournament not found with id: " + dto.getTournamentId()));

        team.setName(dto.getName());
        team.setImgProfile(dto.getImgProfile());
        team.setCity(dto.getCity());
        team.setTournament(tournament);

        return teamRepository.save(team);
    }


}