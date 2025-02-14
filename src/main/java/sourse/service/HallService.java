package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import sourse.dto.request.HallCreationRequest;
import sourse.dto.response.HallResponse;
import sourse.entity.Floor;
import sourse.entity.Hall;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.HallMapper;
import sourse.repository.HallRepository;
import sourse.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class HallService {
    HallMapper hallMapper;
    HallRepository hallRepository;
     FloorService floorService;
    private final UserRepository userRepository;

    public Hall findById(String id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.HALL_NOT_FOUND));
    }
    public HallResponse store(HallCreationRequest request) {
        if(hallRepository.existsByName(request.getName()))
             throw new AppException(ErrorCode.NAME_EXITED);
        Floor floor = floorService.findById(request.getFloorId());

        Hall hall = hallMapper.toHall(request);
        hall.setFloor(floor);
        hall = hallRepository.save(hall);
        return HallResponse.builder()
                .id(hall.getId())
                .name(hall.getName())
                .description(hall.getDescription())
                .floorId(hall.getFloor() != null ? hall.getFloor().getId() : null) 
                .created(hall.getCreated().toString())
                .build();
    }
    public HallResponse show(String id) {
        Hall hall = this.findById(id);
        return hallMapper.toHallResponse(hall);
    }
}
