package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Permission;
import sourse.entity.Seat;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
