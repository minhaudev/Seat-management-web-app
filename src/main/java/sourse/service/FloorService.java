package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import sourse.dto.request.FloorCreationRequest;

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
    public FloorResponse store (FloorCreationRequest request) {
        if(floorRepository.existsByName(request.getName()))
            throw  new AppException(ErrorCode.NAME_EXITED);
        Floor floor = floorMapper.toFloor(request);
        floorRepository.save(floor);
        return floorMapper.toFloorResponse(floor);
    }
    public FloorResponse update (String id, FloorCreationRequest request) {
        Floor floor = this.findById(id);
         floorMapper.updateFloor(floor, request);
        floorRepository.save(floor);
        return floorMapper.toFloorResponse(floor);
    }
    public FloorResponse show (String id) {
        Floor floor = this.findById(id);
        return floorMapper.toFloorResponse(floor);
    }
    public void delete(String id) {
        Floor floor = this.findById(id);
        floorRepository.delete(floor);
    }
    
}
