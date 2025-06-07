package nl.wtrlmn.skm.models;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "img_profile")
    private String imgProfile;

    @Column(nullable = false, name = "city")
    private String city;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    // La lista de jugadores del equipo (squad)
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> squad = new ArrayList<>();

    @Column(name = "points")
    private int pointsTotal = 0;

    @Column(name = "matches")
    private int matchesPlayed = 0;

    @Column(name = "win")
    private int matchesWon = 0;

    @Column(name = "draw")
    private int matchesDrawn = 0;

    @Column(name = "lost")
    private int matchesLost = 0;

    // Relación con partidos jugados como local
    @OneToMany(mappedBy = "teamHome", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matchesHome = new ArrayList<>();

    // Relación con partidos jugados como visitante
    @OneToMany(mappedBy = "teamAway", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matchesAway = new ArrayList<>();

    // Métodos para actualizar estadísticas
    public void updateStatistics(int points, boolean won, boolean drawn, boolean lost) {
        this.pointsTotal += points;
        this.matchesPlayed++;
        if (won) this.matchesWon++;
        if (drawn) this.matchesDrawn++;
        if (lost) this.matchesLost++;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public List<User> getSquad() {
        return squad;
    }

    public void setSquad(List<User> squad) {
        this.squad = squad;
    }

    public int getPointsTotal() {
        return pointsTotal;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public int getMatchesDrawn() {
        return matchesDrawn;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public List<Match> getMatchesLocal() {
        return matchesHome;
    }

    public void setMatchesHome(List<Match> matchesHome) {
        this.matchesHome = matchesHome;
    }

    public List<Match> getMatchesAway() {
        return matchesAway;
    }

    public void setMatchesAway(List<Match> matchesAway) {
        this.matchesAway = matchesAway;
    }
}