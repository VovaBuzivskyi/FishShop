package technikal.task.fishmarket.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import technikal.task.fishmarket.controllers.FishController;
import technikal.task.fishmarket.enums.UserRoleName;
import technikal.task.fishmarket.models.User;
import technikal.task.fishmarket.models.UserRole;
import technikal.task.fishmarket.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(FishController.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Set.of(getUserRoleUSER())));

        User savedUser = userRepository.save(user);
        log.info("User with id: {} registered", savedUser.getId());
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено: " + username));
        log.info("User with id: {} got from repository", user.getId());
        return user;
    }

    private UserRole getUserRoleUSER() {
        UserRole userRole = new UserRole();
        userRole.setRoleName(UserRoleName.USER);
        return userRole;
    }
}
