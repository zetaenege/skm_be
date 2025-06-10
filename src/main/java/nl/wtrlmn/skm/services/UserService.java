package nl.wtrlmn.skm.services;


import nl.wtrlmn.skm.dto.UserInputDTO;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.models.User;
import nl.wtrlmn.skm.repository.TeamRepository;
import nl.wtrlmn.skm.repository.TournamentRepository;
import nl.wtrlmn.skm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;



    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }


    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User createUserFromDTO(UserInputDTO dto) {
        User user = new User();
        fillUserFromDTO(user, dto);
        return userRepository.save(user);
    }

    public User updateUserFromDTO(Long id, UserInputDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        fillUserFromDTO(user, dto);
        return userRepository.save(user);
    }


    private void fillUserFromDTO(User user, UserInputDTO dto) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPosition(dto.getPosition());
        user.setCoach(dto.isCoach());
        user.setAdmin(dto.isAdmin());
        user.setPassword(dto.getPassword());
        user.setImgProfile(dto.getImgProfile());

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + dto.getTeamId()));
            user.setTeam(team);
        } else {
            user.setTeam(null);
        }

        if (dto.getTournamentId() != null) {
            Tournament tournament = tournamentRepository.findById(dto.getTournamentId())
                    .orElseThrow(() -> new IllegalArgumentException("Tournament not found with id: " + dto.getTournamentId()));
            user.setTournament(tournament);
        } else {
            user.setTournament(null);
        }
    }

}
