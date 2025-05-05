package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sourse.dto.response.ProjectNameResponse;
import sourse.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {
    boolean existsByName(String project);
    Optional<Project> findByIdAndName(String id, String name);
    @Query("SELECT new sourse.dto.response.ProjectNameResponse(t.id, t.name) FROM Project t")
    List<ProjectNameResponse> findAllProjectNames();
    Optional<Project> findByName(String name);
}
