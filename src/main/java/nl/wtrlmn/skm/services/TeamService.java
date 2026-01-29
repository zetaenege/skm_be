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


        return dto;
    }




    public TeamOutputDTO updateTeamFromDTO(Long id, TeamInputDTO dto) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));

        if (dto.getName() != null) team.setName(dto.getName());
        if (dto.getImgProfile() != null) team.setImgProfile(dto.getImgProfile());
        if (dto.getCity() != null) team.setCity(dto.getCity());

        if(dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                    .orElseThrow(() -> new RuntimeException("Tournament not found with id: " + dto.getTournamentId()));
            team.setTournament(tournament);
        }
        Team savedTeam = teamRepository.save(team);
        return convertToTeamOutputDTO(savedTeam);
    }


}