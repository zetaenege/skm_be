package nl.wtrlmn.skm.models;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "img_profile", columnDefinition = "TEXT")
    private String imgProfile;


    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "city")
    private String city;

    @OneToMany(
            mappedBy = "tournament",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Team> teams = new ArrayList<>();

    @OneToMany(
            mappedBy = "tournament",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Match> matches = new ArrayList<>();


    public Tournament() {
    }

    public Tournament(String name, String imgProfile, LocalDate startDate, LocalDate endDate, String city) {
        this.name = name;
        this.imgProfile = imgProfile;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
    }


    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return startDate != null && endDate != null
                && !today.isBefore(startDate)
                && !today.isAfter(endDate);
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

    public LocalDate getStartDate() {
       return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if(endDate != null && startDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The start date cannot be later than the end date.");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if(startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("The end date cannot be earlier than the start date.");
        }
        this.endDate = endDate;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}