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

    // --- MÉTODO CORREGIDO ---
    public TeamOutputDTO convertToTeamOutputDTO(Team team) {
        TeamOutputDTO dto = new TeamOutputDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setImgProfile(team.getImgProfile());
        dto.setCity(team.getCity());

        // 1. SQUAD
        if (team.getSquad() != null) {
            List<UserOutputDTO> squadDTOs = team.getSquad().stream().map(user -> {
                UserOutputDTO uDto = userService.convertToUserOutputDTO(user);
                // Si quieres calcular partidos del jugador en el futuro, hazlo aquí.
                // Por ahora, si UserOutputDTO tiene matchesPlayed, ponle 0 o busca la lógica.
                uDto.setMatchesPlayed(0);
                return uDto;
            }).toList();
            dto.setSquad(squadDTOs);
        }

        // 2. ESTADÍSTICAS (POSITION TABLE)
        int played = 0;
        int won = 0;
        int drawn = 0;
        int lost = 0;
        int goalsFor = 0;
        int goalsAgainst = 0;

        // A. Procesar partidos como LOCAL (Home)
        if (team.getMatchesHome() != null) {
            for (Match m : team.getMatchesHome()) {
                // Solo contamos si el partido terminó
                if ("FINISHED".equalsIgnoreCase(m.getStatus())) {
                    played++;

                    // AL SER INT, LOS USAMOS DIRECTAMENTE (Ya no hay error de != null)
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

        // B. Procesar partidos como VISITANTE (Away)
        if (team.getMatchesAway() != null) {
            for (Match m : team.getMatchesAway()) {
                if ("FINISHED".equalsIgnoreCase(m.getStatus())) {
                    played++;

                    int hScore = m.getTeamHomeScore();
                    int aScore = m.getTeamAwayScore();

                    // OJO: Si soy visitante, mis goles a favor son los de Away (aScore)
                    // Y mis goles en contra son los del Local (hScore)
                    goalsFor += aScore;
                    goalsAgainst += hScore;

                    if (aScore > hScore) won++;
                    else if (aScore == hScore) drawn++;
                    else lost++;
                }
            }
        }

        // C. Asignar cálculos al DTO
        dto.setMatchesPlayed(played);
        dto.setWon(won);
        dto.setDrawn(drawn);
        dto.setLost(lost);
        dto.setGoalsFor(goalsFor);
        dto.setGoalsAgainst(goalsAgainst);
        dto.setGoalDifference(goalsFor - goalsAgainst);
        dto.setPoints((won * 3) + drawn); // 3 ptos victoria, 1 pto empate

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