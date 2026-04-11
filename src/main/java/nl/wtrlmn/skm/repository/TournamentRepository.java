
package nl.wtrlmn.skm.repository;

import nl.wtrlmn.skm.models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {


    @Query("SELECT DISTINCT t FROM Tournament t " +
            "LEFT JOIN t.teams tm " +
            "LEFT JOIN tm.squad u " +
            "WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(t.city) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(tm.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Tournament> searchGlobal(@Param("query") String query);

}



