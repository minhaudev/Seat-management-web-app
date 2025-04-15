package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sourse.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRoomId(String roomId);
    Optional<User> findFirstByTeam(String team);
    List<User> findByTeam(String team);
    Optional<User> findFirstByProject(String project);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'SUPERUSER'")
        List<User> findAdmins();


}
