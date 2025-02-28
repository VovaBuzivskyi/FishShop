package technikal.task.fishmarket.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import technikal.task.fishmarket.models.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
}
