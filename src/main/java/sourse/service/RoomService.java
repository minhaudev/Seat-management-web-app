package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.entity.Floor;
import sourse.entity.Hall;
import sourse.entity.Room;
import sourse.entity.User;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.RoomMapper;
import sourse.repository.RoomRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RoomService {
    RoomMapper roomMapper;
    RoomRepository roomRepository;
    HallService hallService;
    UserService userService;

    public Room findById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
    }
    @PostAuthorize("hasRole('SUPERUSER')" )
    public RoomResponse store (RoomCreationRequest request) {

        if(roomRepository.existsByName(request.getName()))
             throw new AppException(ErrorCode.NAME_EXITED);
        User user = userService.findById(request.getOwner());
        Hall hall = hallService.findById(request.getHallId());
        Room room = roomMapper.toRoom(request);
        room.setUser(user);
        room.setHall(hall);
        roomRepository.save(room);
       return  roomMapper.toRoomResponse(room);
    }
    @PreAuthorize("hasRole('SUPERUSER')" )
    public RoomResponse update (String id, RoomUpdateRequest request) {
        Room room = this.findById(id);
        roomMapper.updateRoom(room, request);
        roomRepository.save(room);
        return  roomMapper.toRoomResponse(room);
    }
    @PostAuthorize("hasRole('SUPERUSER')" )
    public RoomResponse show (String  id) {
        Room room = this.findById(id);
        return  roomMapper.toRoomResponse(room);
    }
    @PostAuthorize("hasRole('SUPERUSER')" )
    public void delete (String id) {
        Room room = this.findById(id);
        roomRepository.delete(room);
    }
    @PostAuthorize("hasRole('SUPERUSER')")
    public Page<RoomResponse> index(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> rooms = roomRepository.findAll(pageable);
       return rooms.map(roomMapper::toRoomResponse);
    }
}
