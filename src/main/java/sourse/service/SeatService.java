package sourse.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.request.SeatCreationRequest;
import sourse.dto.request.SeatUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.dto.response.SeatResponse;
import sourse.entity.Room;
import sourse.entity.Seat;
import sourse.entity.User;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.RoomMapper;
import sourse.mapper.SeatMapper;
import sourse.repository.RoomRepository;
import sourse.repository.SeatRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class SeatService {
    SeatMapper seatMapper;
    SeatRepository seatRepository;
    UserService userService;
    RoomService roomService;
    public Seat findById(String id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));
    }
    public SeatResponse  store (SeatCreationRequest request) {
       if(seatRepository.existsByName(request.getName()))
        throw new AppException(ErrorCode.NAME_EXITED);
        User user = userService.findById(request.getUserId());
        Room room = roomService.findById(request.getRoomId());
        Seat seat = seatMapper.toSeat(request);
        seat.setUser(user);
        seat.setRoom(room);
        seatRepository.save(seat);
         return SeatResponse.builder()
                .id(seat.getId())
                .name(seat.getName())
                 .number(seat.getNumber())
                .description(seat.getDescription())
                 .status(seat.getStatus().name())
                 .seatType(seat.getTypeSeat().name())
                .userId(seat.getUser() != null ? seat.getUser().getId() : null)
                .roomId(seat.getRoom() != null ? seat.getRoom().getId() : null)
                .created(seat.getCreated().toString())
                .build();
    }
    
    public SeatResponse  update(String id ,SeatUpdateRequest request) {
        Seat seat = this.findById(id);
        seatMapper.updateSeat(seat,request);
        seatRepository.save(seat);
        return SeatResponse.builder()
                .id(seat.getId())
                .name(seat.getName())
                .description(seat.getDescription())
                .status(seat.getStatus().name())

                .number(seat.getNumber())
                .seatType(seat.getTypeSeat().name())
                .userId(seat.getUser() != null ? seat.getUser().getId() : null)
                .roomId(seat.getRoom() != null ? seat.getRoom().getId() : null)
                .created(seat.getCreated().toString())
                .build();
    }
    public SeatResponse  show(String id) {
        Seat seat = this.findById(id);
        return SeatResponse.builder()
                .id(seat.getId())
                .name(seat.getName())
                .description(seat.getDescription())
                .status(seat.getStatus().name())
                .seatType(seat.getTypeSeat().name())
                .number(seat.getNumber())
                .userId(seat.getUser() != null ? seat.getUser().getId() : null)
                .roomId(seat.getRoom() != null ? seat.getRoom().getId() : null)
                .created(seat.getCreated().toString())
                .build();
    }
    public void  delete(String id) {
        Seat seat = this.findById(id);
        seatRepository.delete(seat);
    }

    
}
