package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sourse.entity.Project;
import sourse.entity.Team;
import sourse.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findByRoomId(String roomId);
    Optional<User> findFirstByTeam_Name(String teamName);
    List<User> findByTeam_Name(String teamName);
    Optional<User> findFirstByProject_Name(String projectName);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'SUPERUSER'")
        List<User> findAdmins();

    Optional<User> findFirstByTeam(Team team);
    Optional<User> findFirstByProject(Project project);

    Optional<User> findFirstByTeam_Id(String teamId);

    Optional<User> findFirstByProject_Id(String projectId);
}
