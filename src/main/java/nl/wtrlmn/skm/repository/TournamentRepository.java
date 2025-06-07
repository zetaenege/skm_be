
package nl.wtrlmn.skm.repository;

import nl.wtrlmn.skm.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}



