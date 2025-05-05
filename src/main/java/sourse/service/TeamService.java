package sourse.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sourse.dto.request.TeamCreationRequest;
import sourse.dto.response.TeamNameResponse;
import sourse.dto.response.TeamResponse;
import sourse.entity.Team;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.FloorMapper;
import sourse.mapper.TeamMapper;
import sourse.repository.TeamRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamMapper teamMapper;
   private final TeamRepository teamRepository ;


    public void validateTeamNameUnique(String name) {
        if (teamRepository.existsByName(name)) {
            throw new AppException(ErrorCode.TEAM_EXISTS);
        }
    }
    public Team validateTeamNameNotFound(String id, String name) {
        return teamRepository.findByIdAndName(id, name)
                .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));
    }
    public  Team findById(String id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTFOUND));

    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public TeamResponse store (TeamCreationRequest request) {
        validateTeamNameUnique(request.getName());
        Team team = teamMapper.toTeam(request);
        teamRepository.save(team);
        return teamMapper.toTeamResponse(team);
    }
    public TeamResponse update(TeamCreationRequest request, String id) {
        Team team = findById(id);
        teamMapper.updateTeamFromRequest(request, team);
        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toTeamResponse(updatedTeam);
    }
    public void delete (String id) {
        Team team = findById(id);
        teamRepository.delete(team);
    }
    public List<TeamResponse> index() {
        return teamMapper.toTeamRepository(teamRepository.findAll());
    }
    public  TeamResponse show (String id) {
        Team team = findById(id);
        return teamMapper.toTeamResponse(team);
    }
    public List<TeamNameResponse> showAllName() {
        return teamRepository.findAllTeamIdAndNames();
    }
}
