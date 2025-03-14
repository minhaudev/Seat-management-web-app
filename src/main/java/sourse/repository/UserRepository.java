package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
