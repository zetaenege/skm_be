package nl.wtrlmn.skm.services;

import jakarta.transaction.Transactional;
import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    public List<Tournament> findAll() {
        return tournamentRepository.findAll();
    }

    public Optional<Tournament> findById(Long id) {
        return tournamentRepository.findById(id);
    }

    public Tournament save(Tournament tournament) {
        return tournamentRepository.save(tournament);
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
}