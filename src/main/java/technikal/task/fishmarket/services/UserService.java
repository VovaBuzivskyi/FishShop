package technikal.task.fishmarket.services;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import technikal.task.fishmarket.controllers.FishController;
import technikal.task.fishmarket.enums.UserRoleName;
import technikal.task.fishmarket.models.User;
import technikal.task.fishmarket.models.UserRole;
import technikal.task.fishmarket.repositories.RoleRepository;
import technikal.task.fishmarket.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(FishController.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public void registerUser(User user) {
        UserRole role = roleRepository.findByRoleName(UserRoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Роль користувача не знайдено"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Set.of(role)));

        User savedUser = userRepository.save(user);
        log.info("User with id: {} registered", savedUser.getId());
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Користувача не знайдено: " + username));
        log.info("User with id: {} got from repository", user.getId());
        return user;
    }
}
