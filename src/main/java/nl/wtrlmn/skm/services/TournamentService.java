package nl.wtrlmn.skm.services;

import jakarta.transaction.Transactional;
import nl.wtrlmn.skm.dto.TeamSimpleDTO;
import nl.wtrlmn.skm.dto.TournamentInputDTO;
import nl.wtrlmn.skm.dto.TournamentOutputDTO;
import nl.wtrlmn.skm.dto.TournamentSimpleDTO;
import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

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


    //Match Generator

    @Transactional
    public void generateMatchesForTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found with id: " + tournamentId));

        List<Team> teams = tournament.getTeams();
        List<Match> newMatches = new ArrayList<>();

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team teamA = teams.get(i);
                Team teamB = teams.get(j);

                // Partido de ida
                Match matchHome = new Match();
                matchHome.setTeamHome(teamA);
                matchHome.setTeamAway(teamB);
                matchHome.setTournament(tournament);
                matchHome.setMatchDate(tournament.getStartDate().atStartOfDay().plusDays(newMatches.size()));
                newMatches.add(matchHome);

                // Partido de vuelta
                Match matchAway = new Match();
                matchAway.setTeamHome(teamB);
                matchAway.setTeamAway(teamA);
                matchAway.setTournament(tournament);
                matchAway.setMatchDate(tournament.getStartDate().atStartOfDay().plusDays(newMatches.size()));
                newMatches.add(matchAway);
            }
        }

        tournament.getMatches().clear();
        tournament.getMatches().addAll(newMatches);
        tournamentRepository.save(tournament);
    }


    // crear from dto
    public TournamentSimpleDTO createTournamentFromDTO(TournamentInputDTO dto) {

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("The tournament name cannot be empty.");
        }

        Tournament tournament = new Tournament();
        tournament.setName(dto.getName());
        tournament.setImgProfile(dto.getImgProfile());
        tournament.setStartDate(dto.getStartDate());
        tournament.setEndDate(dto.getEndDate());
        Tournament savedTournament = tournamentRepository.save(tournament);
        return convertToSimpleDTO(savedTournament);

    }





    // actualizar from dto
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
        return dto;
    }

    private TournamentOutputDTO convertToOutputDTO(Tournament t) {
        TournamentOutputDTO dto = new TournamentOutputDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setImgProfile(t.getImgProfile());
        dto.setStartDate(t.getStartDate());
        dto.setEndDate(t.getEndDate());


        if (t.getTeams() != null) {
            dto.setTeams(t.getTeams().stream().map(team -> {
                TeamSimpleDTO ts = new TeamSimpleDTO();
                ts.setId(team.getId());
                ts.setName(team.getName());
                ts.setImgProfile(team.getImgProfile());
                ts.setCity(team.getCity());
                return ts;
            }).collect(Collectors.toList()));
        }
        return dto;
    }


}