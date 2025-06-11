package nl.wtrlmn.skm.dto;

public class TeamOutputDTO {
    private Long id;
    private String name;
    private String imgProfile;
    private String city;
    private TournamentSimpleDTO tournament;

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

    public TournamentSimpleDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentSimpleDTO tournament) {
        this.tournament = tournament;
    }
}
