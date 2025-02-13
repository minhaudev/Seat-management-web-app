package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
}
