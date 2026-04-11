package nl.wtrlmn.skm.dto;

import java.time.LocalDateTime;

public class MatchOutputDTO {
    private Long id;
    private TeamSimpleDTO homeTeam;
    private TeamSimpleDTO awayTeam;
    private Integer homeScore;
    private Integer awayScore;
    private LocalDateTime matchDate;
    private Long tournamentId;
    private String status;



    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TeamSimpleDTO getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamSimpleDTO homeTeam) {
        this.homeTeam = homeTeam;
    }

    public TeamSimpleDTO getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(TeamSimpleDTO awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }
    public Long getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

}
