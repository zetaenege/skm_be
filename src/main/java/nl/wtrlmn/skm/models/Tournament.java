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

    @Column(name = "img_profile")
    private String imgProfile;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // Relaciones con otras entidades
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    //encontrar jugadores en del torneo [si no sirve quitarlo]
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users = new ArrayList<>();


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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del torneo no puede estar vacío.");
        }
        this.name = name.trim();
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

    /**
     * Establece la fecha de inicio del torneo.
     * Valida que no sea posterior a la fecha de finalización.
     */
    public void setStartDate(LocalDate startDate) {
        if (endDate != null && startDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de finalización.");
        }
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Establece la fecha de finalización del torneo.
     * Valida que no sea anterior a la fecha de inicio.
     */
    public void setEndDate(LocalDate endDate) {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
        }
        this.endDate = endDate;
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

    /**
     * Determina si el torneo está activo hoy.
     * @return true si está activo; false en caso contrario.
     */
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return startDate != null && endDate != null
                && !today.isBefore(startDate)
                && !today.isAfter(endDate);
    }

    /**
     * Equals basado en el id.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tournament)) return false;
        Tournament that = (Tournament) o;
        return Objects.equals(id, that.id);
    }

    /**
     * HashCode basado en la clase y el id.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}