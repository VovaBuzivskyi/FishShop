package technikal.task.fishmarket.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import technikal.task.fishmarket.enums.UserRoleName;
import technikal.task.fishmarket.models.User;
import technikal.task.fishmarket.models.UserRole;
import technikal.task.fishmarket.repositories.RoleRepository;
import technikal.task.fishmarket.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            UserRole userRole = new UserRole();
            userRole.setRoleName(UserRoleName.ROLE_USER);

            UserRole adminRole = new UserRole();
            adminRole.setRoleName(UserRoleName.ROLE_ADMIN);

            List<UserRole> roles = new ArrayList<>(List.of(userRole, adminRole));
            roleRepository.saveAll(roles);

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(new HashSet<>(Set.of(userRole)));

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Set.of(adminRole)));

            List<User> users = new ArrayList<>(List.of(user, admin));
            userRepository.saveAll(users);
        }
    }
}