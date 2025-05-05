package sourse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sourse.dto.request.ProjectCreationRequest;
import sourse.dto.response.ProjectNameResponse;
import sourse.dto.response.ProjectResponse;
import sourse.entity.Project;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.ProjectMapper;
import sourse.repository.ProjectRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectMapper projectMapper;
   private final ProjectRepository projectRepository ;


    public void validateProjectNameUnique(String name) {
        if (projectRepository.existsByName(name)) {
            throw new AppException(ErrorCode.PROJECT_EXISTS);
        }
    }
    public  Project findById(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));

    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public ProjectResponse store (ProjectCreationRequest request) {
        validateProjectNameUnique(request.getName());
        Project project = projectMapper.toProject(request);
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public ProjectResponse update(ProjectCreationRequest request, String id) {
        Project project = findById(id);
        projectMapper.updateProjectFromRequest(request, project);
        Project updatedProject = projectRepository.save(project);
        return projectMapper.toProjectResponse(updatedProject);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public void delete (String id) {
        Project project = findById(id);
        projectRepository.delete(project);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public List<ProjectResponse> index() {
        return projectMapper.toProjectRepository(projectRepository.findAll());
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public  ProjectResponse show (String id) {
        Project project = findById(id);
        return projectMapper.toProjectResponse(project);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public List<ProjectNameResponse> showAllName() {
        return projectRepository.findAllProjectNames();
    }
}
