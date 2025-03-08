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
        UserRole userRole = getRoleByName(UserRoleName.ROLE_USER);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Set.of(userRole)));

        userRepository.save(user);
        log.info("User with id: {} registered", user.getId());
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Користувача не знайдено: " + username));
        log.info("User with id: {} got from repository", user.getId());
        return user;
    }

    public UserRole getRoleByName(UserRoleName roleName) {
        UserRole role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Роль користувача не знайдено: " + roleName.name()));
        log.info("Role: {} got from repository", role.getRoleName());
        return role;
    }
}
