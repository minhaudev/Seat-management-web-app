package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Permission;
import sourse.entity.Role;
import sourse.enums.EnumType;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);

}
