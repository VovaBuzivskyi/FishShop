package technikal.task.fishmarket.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import technikal.task.fishmarket.enums.UserRoleName;
import technikal.task.fishmarket.models.User;
import technikal.task.fishmarket.models.UserRole;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Test
    public void loadUserByUserNameTest() {
        String username = "user";
        String password = "password";

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        UserRole userRole = new UserRole();
        userRole.setRoleName(UserRoleName.ROLE_USER);

        user.setRoles(Set.of(userRole));

        when(userService.getUserByUsername(username)).thenReturn(user);

        UserDetails result = customUserDetailService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                        .equals(UserRoleName.ROLE_USER.name())));

        verify(userService, times(1)).getUserByUsername(username);
    }
}