package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Permission;
import sourse.entity.Seat;
import sourse.entity.User;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, String> {
  boolean existsByName(String permission);
}
