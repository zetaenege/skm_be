package nl.wtrlmn.skm.repository;

import nl.wtrlmn.skm.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTeamHomeIdOrTeamAwayId(Long homeId, Long awayId);
}