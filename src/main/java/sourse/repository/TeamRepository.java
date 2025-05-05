package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sourse.dto.response.TeamNameResponse;
import sourse.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository  extends JpaRepository<Team, String> {
    boolean existsByName(String team);
    Optional<Team> findByIdAndName(String id, String name);
    @Query("SELECT new sourse.dto.response.TeamNameResponse(t.id, t.name) FROM Team t")
    List<TeamNameResponse> findAllTeamIdAndNames();

}
