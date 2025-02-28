package technikal.task.fishmarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technikal.task.fishmarket.enums.UserRoleName;
import technikal.task.fishmarket.models.UserRole;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByRoleName(UserRoleName role);
}
