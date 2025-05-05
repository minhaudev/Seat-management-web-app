package sourse.mapper;

import org.mapstruct.*;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.TeamCreationRequest;
import sourse.dto.response.FloorResponse;
import sourse.dto.response.TeamResponse;
import sourse.entity.Floor;
import sourse.entity.Team;
import sourse.repository.TeamRepository;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toTeam(TeamCreationRequest team);
    TeamResponse toTeamResponse(Team team);
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTeamFromRequest( TeamCreationRequest request, @MappingTarget Team team);
    List<TeamResponse> toTeamRepository(List<Team> teams);
}
