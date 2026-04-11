package nl.wtrlmn.skm.services;

import jakarta.transaction.Transactional;
import nl.wtrlmn.skm.dto.*;
import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TeamService teamService;

    public List<TournamentSimpleDTO> findAll() {
        return tournamentRepository.findAll().stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    public TournamentOutputDTO findById(Long id) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tournament not found"));
        return convertToOutputDTO(tournament);
    }

    public void deleteById(Long id) {
        tournamentRepository.deleteById(id);
    }

    public void generateMatchesForTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with id: " + tournamentId));

        List<Team> teams = tournament.getTeams();
        if (teams.size() < 2) {
            throw new IllegalArgumentException("Not enough teams to generate matches (Need at least 2).");
        }

        List<Match> newMatches = new ArrayList<>();

        List<Team> routingTeams = new ArrayList<>(teams);

        if (routingTeams.size() % 2 != 0) {
            routingTeams.add(null);
        }

        int numTeams = routingTeams.size();
        int totalRounds = numTeams - 1;
        int matchesPerRound = numTeams / 2;

        LocalDateTime currentMatchTime = tournament.getStartDate().atStartOfDay().plusHours(9);


        for (int round = 0; round < totalRounds; round++) {

            for (int match = 0; match < matchesPerRound; match++) {
                Team home = routingTeams.get(match);
                Team away = routingTeams.get(numTeams - 1 - match);

                if (home != null && away != null) {
                    Match matchHome = new Match();
                    matchHome.setTeamHome(home);
                    matchHome.setTeamAway(away);
                    matchHome.setTournament(tournament);
                    matchHome.setMatchDate(currentMatchTime);
                    matchHome.setTeamHomeScore(0);
                    matchHome.setTeamAwayScore(0);
                    matchHome.setStatus("SCHEDULED");

                    newMatches.add(matchHome);

                    currentMatchTime = currentMatchTime.plusHours(1);
                }
            }

            Team lastTeam = routingTeams.remove(numTeams - 1);
            routingTeams.add(1, lastTeam);

            currentMatchTime = currentMatchTime.plusDays(1).withHour(9).withMinute(0);
        }

        int halfSeasonMatches = newMatches.size();

        for (int i = 0; i < halfSeasonMatches; i++) {
            Match ida = newMatches.get(i);
            Match matchAway = new Match();
            matchAway.setTeamHome(ida.getTeamAway());
            matchAway.setTeamAway(ida.getTeamHome());
            matchAway.setTournament(tournament);
            matchAway.setMatchDate(currentMatchTime);
            matchAway.setTeamHomeScore(0);
            matchAway.setTeamAwayScore(0);
            matchAway.setStatus("SCHEDULED");

            newMatches.add(matchAway);

            currentMatchTime = currentMatchTime.plusHours(1);


        }


        if (tournament.getMatches() != null) {
            tournament.getMatches().clear();
            tournament.getMatches().addAll(newMatches);
        } else {
            tournament.setMatches(newMatches);
        }

        tournamentRepository.save(tournament);
    }


    public TournamentSimpleDTO createTournamentFromDTO(TournamentInputDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("The tournament name cannot be empty.");
        }

        Tournament tournament = new Tournament();
        tournament.setName(dto.getName());
        tournament.setImgProfile(dto.getImgProfile());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        tournament.setCity(dto.getCity());
        Tournament savedTournament = tournamentRepository.save(tournament);
        return convertToSimpleDTO(savedTournament);
    }

    public TournamentOutputDTO updateTournamentFromDTO(Long id, TournamentInputDTO dto) {
        Tournament tournament = tournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with id: " + id));

        if (dto.getName() != null) {
            if (dto.getName().isBlank()) {
                throw new IllegalArgumentException("The tournament name cannot be empty.");
            }
            tournament.setName(dto.getName());
        }

        if (dto.getImgProfile() != null) {
            tournament.setImgProfile(dto.getImgProfile());
        }

        if (dto.getStartDate() != null) {
            tournament.setStartDate(dto.getStartDate());
        }

        if (dto.getEndDate() != null) {
            tournament.setEndDate(dto.getEndDate());
        }
        if (dto.getCity() != null) {
            tournament.setCity(dto.getCity());
        }
        Tournament updatedTournament = tournamentRepository.save(tournament);
        return convertToOutputDTO(updatedTournament);
    }

    private TournamentSimpleDTO convertToSimpleDTO(Tournament t) {
        TournamentSimpleDTO dto = new TournamentSimpleDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setImgProfile(t.getImgProfile());
        dto.setStartDate(t.getStartDate());
        dto.setEndDate(t.getEndDate());
        dto.setCity(t.getCity());
        return dto;
    }

    private TournamentOutputDTO convertToOutputDTO(Tournament tournament) {
        TournamentOutputDTO dto = new TournamentOutputDTO();
        dto.setId(tournament.getId());
        dto.setName(tournament.getName());
        dto.setImgProfile(tournament.getImgProfile());
        dto.setStartDate(tournament.getStartDate());
        dto.setEndDate(tournament.getEndDate());
        dto.setActive(tournament.isActive());
        dto.setCity(tournament.getCity());

        if (tournament.getTeams() != null) {
            List<TeamOutputDTO> teamDTOs = tournament.getTeams().stream()
                    .map(team -> teamService.convertToTeamOutputDTO(team))
                    .collect(Collectors.toList());
            dto.setTeams(teamDTOs);
        }
        return dto;
    }

    public List<TournamentOutputDTO> findAllDTOs() {
        List<Tournament> tournaments = tournamentRepository.findAll();
        return tournaments.stream()
                .map(this::convertToOutputDTO)
                .toList();
    }

    public List<TournamentOutputDTO> searchGlobal(String query) {
        List<Tournament> tournaments = tournamentRepository.searchGlobal(query);
        return tournaments.stream()
                .map(this::convertToOutputDTO)
                .collect(Collectors.toList());
    }
}