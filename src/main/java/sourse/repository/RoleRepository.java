package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Permission;
import sourse.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
//    boolean existsByName(String role);
}
