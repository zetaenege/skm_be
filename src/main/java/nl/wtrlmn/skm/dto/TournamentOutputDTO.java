package nl.wtrlmn.skm.dto;

import java.time.LocalDate;
import java.util.List;

public class TournamentOutputDTO {
    private Long id;
    private String name;
    private String imgProfile;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String city;
    private List<TeamOutputDTO> teams;

    public String getCity() {
        return city;
    }

    public List<TeamOutputDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamOutputDTO> teams) {
        this.teams = teams;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

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
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }



}
