package nl.wtrlmn.skm.services;


import nl.wtrlmn.skm.dto.UserInputDTO;
import nl.wtrlmn.skm.dto.UserOutputDTO;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.User;
import nl.wtrlmn.skm.repository.TeamRepository;
import nl.wtrlmn.skm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public List<UserOutputDTO> findAll() {

        return userRepository.findAll().stream()
                .map(this::convertToUserOutputDTO)
                .toList();
    }


    public UserOutputDTO findByIdDTO(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convertToUserOutputDTO(user);
    }



    public UserOutputDTO createUserFromDTO(UserInputDTO dto) {
        User user = new User();
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use: " + dto.getEmail());
        }
        fillUserFromDTO(user, dto);
        User savedUser = userRepository.save(user);
        return convertToUserOutputDTO(savedUser);
    }


    public UserOutputDTO updateUserFromDTO(Long id, UserInputDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        fillUserFromDTO(user, dto);
        User updatedUser = userRepository.save(user);
        return convertToUserOutputDTO(updatedUser);
    }

    public void deleteById(Long id) {

        userRepository.deleteById(id);
    }


    public UserService(
            UserRepository userRepository,
            TeamRepository teamRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }


    private void fillUserFromDTO(User user, UserInputDTO dto) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPosition(dto.getPosition());
        user.setCoach(dto.isCoach());
        user.setAdmin(dto.isAdmin());
        user.setImgProfile(dto.getImgProfile());


        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }


        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found with id: " + dto.getTeamId()));
            user.setTeam(team);
        }

    }
    public UserOutputDTO convertToUserOutputDTO(User user) {
        UserOutputDTO dto = new UserOutputDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setImgProfile(user.getImgProfile());
        dto.setPosition(user.getPosition());
        dto.setCoach(user.isCoach());
        dto.setAdmin(user.isAdmin());

        if (user.getTeam() != null) {
            dto.setTeamId(user.getTeam().getId());

            if (user.getTeam().getTournament() != null) {
                dto.setTournamentId(user.getTeam().getTournament().getId());
            }

        }

        return dto;
    }

    public UserOutputDTO findByEmailDTO(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return convertToUserOutputDTO(user);
    }

}
