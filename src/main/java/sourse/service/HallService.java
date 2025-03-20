package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sourse.dto.request.HallCreationRequest;
import sourse.dto.request.HallUpdateRequest;
import sourse.dto.response.HallResponse;
import sourse.entity.Floor;
import sourse.entity.Hall;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.HallMapper;
import sourse.repository.HallRepository;
import sourse.repository.UserRepository;

import java.util.List;

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
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public HallResponse store(HallCreationRequest request) {
        if(hallRepository.existsByName(request.getName()))
             throw new AppException(ErrorCode.NAME_EXITED);
        Floor floor = floorService.findById(request.getFloorId());

        Hall hall = hallMapper.toHall(request);
        hall.setFloor(floor);
        hall = hallRepository.save(hall);
        return  hallMapper.toHallResponse(hall);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public HallResponse show(String id) {
        Hall hall = this.findById(id);
        return hallMapper.toHallResponse(hall);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public HallResponse update(String id, HallUpdateRequest request) {
        Hall hall = this.findById(id);
        hallMapper.updateHall(hall, request);
        hallRepository.save(hall);
        return hallMapper.toHallResponse(hall);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public void delete(String id) {
        Hall hall = this.findById(id);
        hallRepository.delete(hall);
        hallMapper.toHallResponse(hall);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public   List<HallResponse> index (){
        return hallMapper.toHallResponseList( hallRepository.findAll());
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public List <HallResponse> getAllFloor(String id) {
        Floor floor = floorService.findById(id);
        List<Hall>  halls =  hallRepository.findByFloorId(floor.getId());
        return hallMapper.toHallResponseList(halls);
    }
}
