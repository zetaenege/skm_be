package nl.wtrlmn.skm.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    @JsonIgnoreProperties({"teams", "matches"})
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "team_home_id", nullable = false)
    @JsonIgnoreProperties({"matchesHome", "matchesAway", "tournament", "squad"})
    private Team teamHome;

    @ManyToOne
    @JoinColumn(name = "team_away_id", nullable = false)
    @JsonIgnoreProperties({"matchesHome", "matchesAway", "tournament", "squad"})
    private Team teamAway;


    @Column(name = "team_home_points")
    private int teamHomePoints;

    @Column(name = "team_away_points")
    private int teamAwayPoints;


    @Column(name = "team_home_score")
    private int teamHomeScore;

    @Column(name = "team_away_score")
    private int teamAwayScore;



    // Asignacion de puntos a los equipos
    private void assignPoints(Team winner, Team loser, int winnerPoints, int loserPoints,
                              boolean won, boolean drawn, boolean lost) {
        if (teamHome == winner) {
            this.teamHomePoints = winnerPoints;
            this.teamAwayPoints = loserPoints;
        } else {
            this.teamHomePoints = loserPoints;
            this.teamAwayPoints = winnerPoints;
        }

        winner.updateStatistics(winnerPoints, won, drawn, lost);
        loser.updateStatistics(loserPoints, false, drawn, true);
    }


    public void calculatePoints() {
        validateTeamsAndGoals();

        if (teamHomeScore > teamAwayScore) {
            assignPoints(teamHome, teamAway, 3, 0, true, false, false);
        } else if (teamAwayScore > teamHomeScore) {
            assignPoints(teamAway, teamHome, 3, 0, true, false, false);
        } else {
            assignPoints(teamHome, teamAway, 1, 1, false, true, false);
        }
    }

    private void validateTeamsAndGoals() {
        if (teamHome == null || teamAway == null) {
            throw new IllegalStateException("Both teams must be defined to calculate points.");
        }

        if (teamHomeScore < 0 || teamAwayScore < 0) {
            throw new IllegalArgumentException("Goals cannot be negative.");
        }
    }




    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public Team getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(Team teamAway) {
        this.teamAway = teamAway;
    }

    public Team getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(Team teamHome) {
        this.teamHome = teamHome;
    }

    public int getTeamHomePoints() {
        return teamHomePoints;
    }

    public void setTeamHomePoints(int teamHomePoints) {
        if (teamHomePoints < 0) {
            throw new IllegalArgumentException("Points cannot be negative.");
        }
        this.teamHomePoints = teamHomePoints;
    }

    public int getTeamAwayPoints() {
        return teamAwayPoints;
    }

    public void setTeamAwayPoints(int teamAwayPoints) {
        if (teamAwayPoints < 0) {
            throw new IllegalArgumentException("Points cannot be negative.");
        }
        this.teamAwayPoints = teamAwayPoints;
    }

    public int getTeamHomeScore() {
        return teamHomeScore;
    }

    public void setTeamHomeScore(int teamHomeScore) {
        if (teamHomeScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }
        this.teamHomeScore = teamHomeScore;
    }

    public int getTeamAwayScore() {
        if (teamAwayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }
        return teamAwayScore;
    }

    public void setTeamAwayScore(int teamAwayScore) {
        this.teamAwayScore = teamAwayScore;
    }
}