package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRoomId(String roomId);
    Optional<User> findFirstByTeam(String team);
    List<User> findByTeam(String team);
    Optional<User> findFirstByProject(String project);
}
