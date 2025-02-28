package technikal.task.fishmarket.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import technikal.task.fishmarket.enums.UserRoleName;

@Entity
@Table(name = "roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", unique = true, nullable = false)
    private UserRoleName roleName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserRoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(UserRoleName role) {
        this.roleName = role;
    }
}
