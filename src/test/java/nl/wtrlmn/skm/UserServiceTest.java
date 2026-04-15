package nl.wtrlmn.skm.services;

import nl.wtrlmn.skm.dto.UserInputDTO;
import nl.wtrlmn.skm.dto.UserOutputDTO;
import nl.wtrlmn.skm.models.Team;
import nl.wtrlmn.skm.models.Tournament;
import nl.wtrlmn.skm.models.User;
import nl.wtrlmn.skm.repository.TeamRepository;
import nl.wtrlmn.skm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindAll_Success() {
        User user = new User(); user.setId(1L);
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserOutputDTO> result = userService.findAll();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindById_Success() {
        User user = new User(); user.setId(1L); user.setEmail("test@test.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserOutputDTO result = userService.findByIdDTO(1L);

        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    public void testFindById_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findByIdDTO(99L));
    }

    @Test
    public void testCreateUserFromDTO_Success() {
        UserInputDTO dto = new UserInputDTO();
        dto.setEmail("new@test.com");
        dto.setPassword("pass");

        User savedUser = new User();
        savedUser.setId(1L);

        when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserOutputDTO result = userService.createUserFromDTO(dto);

        assertEquals(1L, result.getId());
    }

    @Test
    public void testCreateUserFromDTO_EmailExists() {
        UserInputDTO dto = new UserInputDTO();
        dto.setEmail("exist@test.com");
        when(userRepository.findByEmail("exist@test.com")).thenReturn(Optional.of(new User()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.createUserFromDTO(dto));
        assertTrue(ex.getMessage().contains("Email already in use"));
    }

    @Test
    public void testUpdateUserFromDTO_Success() {
        UserInputDTO dto = new UserInputDTO();
        dto.setTeamId(1L);

        User existingUser = new User(); existingUser.setId(1L);
        Team team = new Team(); team.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserOutputDTO result = userService.updateUserFromDTO(1L, dto);

        assertEquals(1L, result.getId());
    }

    @Test
    public void testUpdateUserFromDTO_NotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUserFromDTO(99L, new UserInputDTO()));
    }

    @Test
    public void testUpdateUserFromDTO_TeamNotFound() {
        // Arrange
        UserInputDTO dto = new UserInputDTO();
        dto.setTeamId(99L);
        User existingUser = new User(); existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(teamRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUserFromDTO(1L, dto));
    }

    @Test
    public void testDeleteById_Success() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByEmail_Success() {
        User user = new User(); user.setEmail("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        UserOutputDTO result = userService.findByEmailDTO("test@test.com");

        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    public void testFindByEmail_NotFound() {
        when(userRepository.findByEmail("not@found.com")).thenReturn(Optional.empty());


        assertThrows(IllegalArgumentException.class, () -> userService.findByEmailDTO("not@found.com"));
    }

    @Test
    public void testConvertToOutputDTO_WithFullData() {

        Tournament tournament = new Tournament(); tournament.setId(1L);
        Team team = new Team(); team.setId(1L); team.setTournament(tournament);
        User user = new User(); user.setId(1L); user.setTeam(team);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserOutputDTO result = userService.findByIdDTO(1L);

        assertEquals(1L, result.getTeamId());
        assertEquals(1L, result.getTournamentId());
    }
}