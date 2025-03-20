package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sourse.dto.request.FloorCreationRequest;

import sourse.dto.request.FloorUpdateRequest;
import sourse.dto.response.FloorResponse;
import sourse.dto.response.RoomResponse;
import sourse.entity.Floor;
import sourse.entity.Room;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.FloorMapper;
import sourse.mapper.RoomMapper;
import sourse.repository.FloorRepository;
import sourse.repository.RoomRepository;
import sourse.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class FloorService {
    FloorMapper floorMapper;
    FloorRepository floorRepository;
   

    public Floor findById(String id) {
        return floorRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.FLOOR_NOT_FOUND));
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public FloorResponse store (FloorCreationRequest request) {

        if(floorRepository.existsByName(request.getName()))
            throw  new AppException(ErrorCode.NAME_EXITED);
        Floor floor = floorMapper.toFloor(request);
        floorRepository.save(floor);
        return floorMapper.toFloorResponse(floor);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public List <FloorResponse> index () {
        return floorMapper.toFloorResponseList(floorRepository.findAll());
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public FloorResponse update (String id, FloorUpdateRequest request) {
        Floor floor = this.findById(id);
         floorMapper.updateFloor(floor, request);
        floorRepository.save(floor);
        return floorMapper.toFloorResponse(floor);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public FloorResponse show (String id) {
        Floor floor = this.findById(id);
        return floorMapper.toFloorResponse(floor);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public void delete(String id) {
        Floor floor = this.findById(id);
        floorRepository.delete(floor);
    }
    
}
