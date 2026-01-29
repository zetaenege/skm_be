package nl.wtrlmn.skm.services;


import jakarta.persistence.EntityNotFoundException;
import nl.wtrlmn.skm.dto.MatchOutputDTO;
import nl.wtrlmn.skm.dto.TeamOutputDTO;
import nl.wtrlmn.skm.dto.TeamSimpleDTO;
import nl.wtrlmn.skm.models.Match;
import nl.wtrlmn.skm.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    public List<MatchOutputDTO> findAll() {

        return matchRepository.findAll().stream()
                .map(this::convertToOutputDTO)
                .toList();
    }

    public MatchOutputDTO findByIdDTO(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        return convertToOutputDTO(match);
    }


    public void deleteById(Long id) {

        matchRepository.deleteById(id);
    }

    private MatchOutputDTO convertToOutputDTO(Match match) {
        MatchOutputDTO dto = new MatchOutputDTO();
        dto.setId(match.getId());
        dto.setMatchDate(match.getMatchDate());

      if (match.getTeamHome() != null) {
          TeamSimpleDTO home = new TeamSimpleDTO();
          home.setId(match.getTeamHome().getId());
          home.setId(match.getTeamHome().getId());
          home.setName(match.getTeamHome().getName());
          home.setImgProfile(match.getTeamHome().getImgProfile());
          dto.setHomeTeam(home);
      }
      if (match.getTeamAway() != null) {
          TeamSimpleDTO away = new TeamSimpleDTO();
          away.setId(match.getTeamAway().getId());
          away.setName(match.getTeamAway().getName());
          away.setImgProfile(match.getTeamAway().getImgProfile());
          dto.setAwayTeam(away);
      }

      if (match.getTournament() != null) {
          dto.setTournamentId(match.getTournament().getId());

      }

        return dto;
    }

    public MatchOutputDTO saveFromDTO(Match match) {
        Match savedMatch = matchRepository.save(match);
        return convertToOutputDTO(savedMatch);
    }

    public List<MatchOutputDTO> findMatchesByTeam(Long teamId) {
        return matchRepository.findByTeamHomeIdOrTeamAwayId(teamId,teamId)
                .stream()
                .map(this::convertToOutputDTO)
                .toList();
    }

}
