package sourse.mapper;

import org.mapstruct.*;
import sourse.dto.request.ProjectCreationRequest;
import sourse.dto.response.ProjectResponse;
import sourse.entity.Project;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toProject(ProjectCreationRequest project);
    ProjectResponse toProjectResponse(Project project);
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectFromRequest( ProjectCreationRequest request, @MappingTarget Project project);
    List<ProjectResponse> toProjectRepository(List<Project> projects);
}
