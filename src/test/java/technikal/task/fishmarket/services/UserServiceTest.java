package technikal.task.fishmarket.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import technikal.task.fishmarket.enums.UserRoleName;
import technikal.task.fishmarket.models.User;
import technikal.task.fishmarket.models.UserRole;
import technikal.task.fishmarket.repositories.RoleRepository;
import technikal.task.fishmarket.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserTest() {
        long userId = 1L;
        String username = "user";
        String password = "password";
        String encodedPassword = "encodedPassword";

        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);

        UserRole userRole = new UserRole();
        userRole.setRoleName(UserRoleName.ROLE_USER);

        when(roleRepository.findByRoleName(UserRoleName.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        userService.registerUser(user);

        assertEquals(encodedPassword, user.getPassword());
        assertTrue(user.getRoles().contains(userRole));
        verify(userRepository).save(user);
    }

    @Test
    void registerUserThrowsExceptionTest() {
        when(roleRepository.findByRoleName(UserRoleName.ROLE_USER))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.registerUser(new User()));
    }

    @Test
    void getUserByUsernameTest() {
        long userId = 1L;
        String username = "user";
        String password = "password";

        User user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername(username);

        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
    }

    @Test
    void getUserByUsernameThrowsExceptionTest() {
        String username = "user";
        when(userRepository.findByUsername(username)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByUsername(username));
    }
}