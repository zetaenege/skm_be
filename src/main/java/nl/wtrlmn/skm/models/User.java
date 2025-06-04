package nl.wtrlmn.skm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false, name = "name")
    private String name;

    @Email(message = "Debe ser un email válido.")
    @NotBlank(message = "El email no puede estar vacío.")
    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(name = "img_profile")
    private String imgProfile;

    @Column(name = "position")
    private String position;

    @Column(name = "is_coach")
    private boolean isCoach;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @JsonIgnore // Para no exponer la contraseña en JSON
    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Column(nullable = false, name = "password")
    private String password;

    // Constructores
    public User() {}

    public User(String name, String email, String position, boolean isCoach, boolean isAdmin, Team team, Tournament tournament, String password, String imgProfile) {
        this.name = name;
        this.email = email;
        this.position = position;
        this.isCoach = isCoach;
        this.isAdmin = isAdmin;
        this.team = team;
        this.tournament = tournament;
        this.password = password;
        this.imgProfile = imgProfile;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isCoach() {
        return isCoach;
    }

    public void setCoach(boolean coach) {
        isCoach = coach;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }
}