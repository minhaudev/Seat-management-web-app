package sourse.service;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import sourse.dto.request.SeatCreationRequest;
import sourse.dto.request.SeatUpdatePositionRequest;
import sourse.dto.request.SeatUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.dto.response.SeatResponse;
import sourse.dto.response.SeatUserResponse;
import sourse.dto.response.UserResponse;
import sourse.entity.*;
import sourse.enums.EnumType;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.SeatMapper;
import sourse.mapper.UserMapper;
import sourse.repository.SeatRepository;
import sourse.repository.UserRepository;
import sourse.util.UserUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class SeatService {
    SeatMapper seatMapper;
    SeatRepository seatRepository;
    UserService userService;
    RoomService roomService;
    AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    WebSocketService webSocketService;
    private final RedisService redisService;

    public Seat findById(String id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));
    }
    private void validateLandlordPermission(Room room) {
        authenticationService.checkLandlordPermission(room.getOwnerId());
    }
    private final ApplicationContext applicationContext;

    private UserService getUserService() {
        return applicationContext.getBean(UserService.class);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")
    public SeatResponse store(SeatCreationRequest request) {
       Room room = roomService.findById(request.getRoomId());
//        validateLandlordPermission(room);
        Seat seat = seatMapper.toSeat(request);
        seat.setRoom(room);
        if (request.getUserId() != null) {
            User user = getUserService().findById(request.getUserId());
            if (seatRepository.existsByUser(user)) {
                throw new AppException(ErrorCode.SEAT_ALREADY_ASSIGNED);
            }
            seat.setUser(user);
            seat.setStatus(EnumType.SeatStatus.OCCUPIED);
        } else {
            seat.setStatus(EnumType.SeatStatus.AVAILABLE);
        }
        int maxNumber = seatRepository.findMaxNumber();
        seat.setNumber(String.valueOf(maxNumber + 1));
        seatRepository.save(seat);
        return seatMapper.toSeatResponse(seat);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")
    public SeatResponse update(String id, SeatUpdateRequest request) {
        Seat seat = this.findById(id);
        Room room = seat.getRoom();
        validateLandlordPermission(room);
        seatMapper.updateSeat(seat, request);
        seatRepository.save(seat);
        SeatResponse seatResponse = seatMapper.toSeatResponse(seat);

//        webSocketService.sendSeatUpdateNotification(room.getId(), seatMapper.toSeatResponse(seat));

        return seatResponse;
    }

//    @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")

    public SeatResponse  show(String id) {
        Seat seat = this.findById(id);
        Room room = seat.getRoom();
        validateLandlordPermission(room);
        return seatMapper.toSeatResponse(seat);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")
    public void  delete(String id) {
        Seat seat = this.findById(id);
        Room room = seat.getRoom();

        validateLandlordPermission(room);

        seatRepository.delete(seat);
    }
//    @PreAuthorize("hasAnyRole('SUPERUSER')")
    public Map<String, Object> index(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Seat> seats = seatRepository.findAll(pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("content", seatMapper.toSeatResponseList(seats.getContent()));
        response.put("pageNumber", seats.getNumber());
        response.put("totalPages", seats.getTotalPages());
        response.put("totalElements", seats.getTotalElements());
        return response;
    }
    @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")
    public SeatResponse assignment(String id, String userId, EnumType.TypeSeat typeSeat, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime expiredAt) {
        Seat seat = this.findById(id);
        var user = userService.findById(userId);

        if(seat.getUser() != null) {
            throw new AppException(ErrorCode.SEAT_TAKEN);
        }

        Room room = seat.getRoom();

        seat.setTypeSeat(typeSeat);
        seat.setUser(user);
        seat.setStatus(EnumType.SeatStatus.OCCUPIED);

        if (typeSeat == EnumType.TypeSeat.TEMPORARY) {
            seat.setExpiredAt(expiredAt);
        } else {
            seat.setExpiredAt(null);
        }

        seatRepository.save(seat);

        SeatResponse response = seatMapper.toSeatResponse(seat);
        webSocketService.sendSeatUpdateNotification(room.getId(), response, "seat", "Your room has been updated.");
        return response;
    }

    @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")
    public  SeatResponse reAssignment(String id, String idNewSeat){
        Seat oldSeat = this.findById(id);
        Seat newSeat = this.findById(idNewSeat);
        var roomId = newSeat.getRoom().getId();
        if (oldSeat.getUser() == null) {
            throw new AppException(ErrorCode.NO_USER_IN_SEAT);
        } else if(newSeat.getUser() != null){
            throw new AppException(ErrorCode.SEAT_ALREADY_ASSIGNED);
        } else if(oldSeat.getTypeSeat().name() == "PERMANENT"){
            throw  new AppException(ErrorCode.SEAT_NOT_CHANGE);
        }

        User user = oldSeat.getUser();
        oldSeat.setUser(null);
        newSeat.setUser(user);
        newSeat.setStatus(EnumType.SeatStatus.OCCUPIED);
        oldSeat.setStatus(EnumType.SeatStatus.AVAILABLE);
        seatRepository.save(oldSeat);
        seatRepository.save(newSeat);
        webSocketService.sendSeatUpdateNotification(roomId, seatMapper.toSeatResponse(newSeat),"seat", "Your room has been updated.");
        return seatMapper.toSeatResponse(newSeat);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")
    public SeatResponse removeSeat(String id ) {
        Seat seat = this.findById(id);
        if(seat.getUser() == null) {
            throw new AppException(ErrorCode.NO_USER_IN_SEAT);
        }
        Room room = seat.getRoom();
        seat.setStatus(EnumType.SeatStatus.AVAILABLE);
        seat.setUser(null);
        seat.setExpiredAt(null);
        seatRepository.save(seat);
        SeatResponse response = seatMapper.toSeatResponse(seat);
        webSocketService.sendSeatUpdateNotification(room.getId(), seatMapper.toSeatResponse(seat),"seat", "Your room has been updated.");
        return  response;

    }

    public SeatUserResponse seatUser (String id){
        Seat seat = this.findById(id);
        User user = (seat.getUser() != null) ? userService.findById(seat.getUser().getId()) : null;
        if (user == null) {
            throw  new AppException(ErrorCode.NO_USER_IN_SEAT);
        }

        return SeatUserResponse.builder()
                .id(seat.getId())
                .name(seat.getName())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .project(user != null && user.getProject() != null ? user.getProject().getName() : null)
                .team(user != null && user.getTeam() != null ? user.getTeam().getName() : null)
                .typeSeat(seat.getTypeSeat().name())
                .status(seat.getStatus().name())
                .roomId(seat.getRoom().getId())
                .number(seat.getNumber())
                .created(seat.getCreated().toString())
                .build();
    }
//        @PreAuthorize("hasAnyRole('SUPERUSER', 'LANDLORD')")
//        @Transactional
//        public void updatePositionSeats(List<SeatUpdatePositionRequest> seatRequests, String roomId) {
//            Room room = roomService.findById(roomId);
//            List<Seat> seatList = new ArrayList<>();
//
//            for (SeatUpdatePositionRequest seatRequest : seatRequests) {
//                Seat seat = this.findById(seatRequest.getId());
//                if (seat != null) {
//                    seat.setOx(seatRequest.getOx());
//                    seat.setOy(seatRequest.getOy());
//                    seatList.add(seat);
//                    seatRepository.save(seat);
//                }
//            }
//        }

    @PreAuthorize("hasAnyRole('SUPERUSER','LANDLORD')")
    @Transactional
    public List<SeatResponse>  updatePositionSeats(List<SeatUpdatePositionRequest> seatRequests, String roomId) {
        Room room = roomService.findById(roomId);
        List<Seat> seatList = new ArrayList<>();

        for (SeatUpdatePositionRequest seatRequest : seatRequests) {
            Seat seat = this.findById(seatRequest.getId());
            if (seat != null) {
                seat.setOx(seatRequest.getOx());
                seat.setOy(seatRequest.getOy());
                seatList.add(seat);
            }
        }

        String role = UserUtils.getCurrentUserRole();
        List<SeatResponse> seatResponseList = null;
        if ("LANDLORD".equals(role)) {
            RoomChange existingChange = redisService.getRoomChange("room_changes", roomId);

            seatResponseList = seatList.stream()
                    .map(seat -> SeatResponse.builder()
                            .id(seat.getId())
                            .name(seat.getName())
                            .user(seat.getUser() != null ? UserResponse.builder()
                                    .id(seat.getUser().getId())
                                    .firstName(seat.getUser().getFirstName())
                                    .lastName(seat.getUser().getLastName())
                                    .email(seat.getUser().getEmail())
                                    .color(seat.getUser().getColor())
                                    .build() : null)
                            .roomId(seat.getRoom() != null ? seat.getRoom().getId() : null)
                            .typeSeat(String.valueOf(seat.getTypeSeat()))
                            .status(String.valueOf(seat.getStatus()))
                            .ox(seat.getOx())
                            .oy(seat.getOy())
                            .number(seat.getNumber())
                            .description(seat.getDescription())
                            .expiredAt(seat.getExpiredAt() != null ? seat.getExpiredAt().toString() : null)
                            .build())
                    .toList();

            if (existingChange != null) {
                existingChange.setSeatData(seatResponseList);
            } else {
                existingChange = RoomChange.builder()
                        .roomId(roomId)
                        .roomName(room.getName())
                        .changedBy("LANDLORD")
                        .seatData(seatResponseList)
                        .status("PENDING")
                        .build();
            }
            redisService.addOrUpdateRoomChange("room_changes", existingChange);
            webSocketService.sendToSuperUsers(null, "SUPERUSER", "A landlord has updated seat positions. Please review.");
        } else if ("SUPERUSER".equals(role)) {
            for (Seat seat : seatList) {
                seatRepository.save(seat);
            }
            webSocketService.sendSeatUpdateNotification(roomId, null, "seat", "Seat positions have been updated directly by SUPERUSER.");
        }

        return seatResponseList;
    }



//    @PreAuthorize("hasAnyRole('LANDLORD')")
    public Page <SeatResponse> listSeatInRoom (String id,int page, int size, EnumType.SeatStatus status) {
        Page<Seat> seats;
        Room room = roomService.findById(id);
//        validateLandlordPermission(room);

        Pageable pageable = PageRequest.of(page, size);
        if (status != null) {
            seats = seatRepository.findByRoomIdAndStatus(room.getId(), status, pageable);
        } else {
            seats = seatRepository.findByRoomId(room.getId(), pageable);
        }
        return seats.map(seatMapper::toSeatResponse);
    }
}
