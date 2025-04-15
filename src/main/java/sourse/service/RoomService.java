package sourse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sourse.dto.request.RoomCreationRequest;
import sourse.dto.request.RoomUpdateRequest;
import sourse.dto.response.RoomResponse;
import sourse.entity.Hall;
import sourse.entity.Room;
import sourse.entity.RoomChange;
import sourse.entity.User;
import sourse.exception.AppException;
import sourse.exception.ErrorCode;
import sourse.mapper.RoomMapper;
import sourse.repository.FloorRepository;
import sourse.repository.RoomRepository;
import jakarta.annotation.PostConstruct;
import sourse.util.UserUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.PrivateKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RoomService {
    RoomMapper roomMapper;
    RoomRepository roomRepository;
    RedisService redisService;
    HallService hallService;
    @Lazy
    private final UserService userService;
    private final FloorRepository floorRepository;
    private final WebSocketService webSocketService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public Room findById(String id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
    }
    @PreAuthorize("hasRole('SUPERUSER')")
    public RoomResponse store (RoomCreationRequest request) {
        if(roomRepository.existsByName(request.getName()))
             throw new AppException(ErrorCode.NAME_EXITED);
        User user = userService.findById(request.getOwner());
        Hall hall = hallService.findById(request.getHallId());
        Room room = roomMapper.toRoom(request);
        room.setNameOwner(user.getFirstName() + user.getLastName());
        room.setUser(user);
        room.setHall(hall);
        roomRepository.save(room);
       return  roomMapper.toRoomResponse(room);
    }
    @PreAuthorize("hasRole('SUPERUSER')" )
    public RoomResponse update (String id, RoomUpdateRequest request) {
        Room room = this.findById(id);
        User user = userService.findById(request.getOwner());
        room.setNameOwner(user.getFirstName() + user.getLastName());
        roomMapper.updateRoom(room, request);
        roomRepository.save(room);
        return  roomMapper.toRoomResponse(room);
    }
//    @PreAuthorize("hasRole('SUPERUSER')")
    public RoomResponse show (String  id) {
        Room room = this.findById(id);
        return  roomMapper.toRoomResponse(room);
    }
    @PreAuthorize("hasRole('SUPERUSER')")
    public void delete (String id) {
        Room room = this.findById(id);
        roomRepository.delete(room);
    }
    @PreAuthorize("hasRole('SUPERUSER')")
    public Page<RoomResponse> index(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> rooms = roomRepository.findAll(pageable);
       return rooms.map(roomMapper::toRoomResponse);
    }
    @PreAuthorize("hasRole('SUPERUSER')")
    public List<RoomResponse> roomInHall(String id) {
        Hall hall = hallService.findById(id);
        List<Room> rooms = roomRepository.findByHallId(hall.getId());
        return rooms.stream().map(roomMapper::toRoomResponse).collect(Collectors.toList());
    }
    @PreAuthorize("hasAnyRole('SUPERUSER','LANDLORD')")
    public RoomResponse updateRoomObjects(String roomId, List<Room.ObjectData> newObjects) {
                Room room = this.findById(roomId);
            List<Room.ObjectData> currentObjects = room.getObject();
            if (currentObjects == null) {
                currentObjects = new ArrayList<>();
            }
            Map<UUID, Room.ObjectData> objectMap = currentObjects.stream()
                    .collect(Collectors.toMap(Room.ObjectData::getId, obj -> obj));
            for (Room.ObjectData newObj : newObjects) {
                if (newObj.getId() != null && objectMap.containsKey(newObj.getId())) {
                    Room.ObjectData existingObj = objectMap.get(newObj.getId());
                    existingObj.setName(newObj.getName());
                    existingObj.setWidth(newObj.getWidth());
                    existingObj.setHeight(newObj.getHeight());
                    existingObj.setOx(newObj.getOx());
                    existingObj.setOy(newObj.getOy());
                    existingObj.setColor(newObj.getColor());
                } else {
                    currentObjects.add(newObj);
                }
            }
            room.setObject(currentObjects);
            String role = UserUtils.getCurrentUserRole();
            if("LANDLORD" .equals(role)) {
                RoomChange roomChange = new RoomChange(roomId,room.getName(), "LANDLORD", newObjects, "PENDING");
                redisService.addOrUpdateRoomChange("room_changes", roomChange);
                webSocketService.sendToSuperUsers(null,  "SUPERUSER", "A landlord has updated the room. Please review.");
                return roomMapper.toRoomResponse(room);
            } else if ("SUPERUSER".equals(role)) {
            roomRepository.save(room);
            webSocketService.sendSeatUpdateNotification(roomId, null, "object", "Your room has been updated.");
                return roomMapper.toRoomResponse(room);
            };
            return roomMapper.toRoomResponse(room);
        }


public RoomResponse updateRoomApprove(String roomId, List<Room.ObjectData> newObjects) {
    Room room = this.findById(roomId);
    List<Room.ObjectData> currentObjects = room.getObject();
    if (currentObjects == null) {
        currentObjects = new ArrayList<>();
    }
    Map<UUID, Room.ObjectData> objectMap = currentObjects.stream()
            .collect(Collectors.toMap(Room.ObjectData::getId, obj -> obj));
    for (Room.ObjectData newObj : newObjects) {
        if (newObj.getId() != null && objectMap.containsKey(newObj.getId())) {
            Room.ObjectData existingObj = objectMap.get(newObj.getId());
            existingObj.setName(newObj.getName());
            existingObj.setWidth(newObj.getWidth());
            existingObj.setHeight(newObj.getHeight());
            existingObj.setOx(newObj.getOx());
            existingObj.setOy(newObj.getOy());
            existingObj.setColor(newObj.getColor());
        } else {
            currentObjects.add(newObj);
        }
    }
         room.setObject(currentObjects);
        roomRepository.save(room);
        webSocketService.sendSeatUpdateNotification(roomId, null, "object", "Your room has been updated.");
        return roomMapper.toRoomResponse(room);

}
        @PreAuthorize("hasAnyRole('SUPERUSER')")
            public void approve(String roomId) {
            Optional<RoomChange> roomChangeOpt = redisService.getRoomChangeById("room_changes",roomId);
            if(roomChangeOpt.isEmpty()) {
                throw  new AppException(ErrorCode.ROOM_NOT_FOUND);
            }
            RoomChange roomChange = roomChangeOpt.get();
            updateRoomApprove(roomId, roomChange.getChangedData());
            redisService.removeRoomChange("room_changes", roomId);

        }

    @PreAuthorize("hasAnyRole('SUPERUSER','LANDLORD')")
    public RoomResponse uploadImage(String roomId, MultipartFile file) {
        Room room = this.findById(roomId);

        // Tạo đường dẫn thư mục lưu ảnh
        String uploadDir = "uploads";
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


            room.setImage("/images/" + file.getOriginalFilename());
            roomRepository.save(room);
        } catch (Exception e) {
            throw new RuntimeException("Không thể lưu file: " + e.getMessage());
        }

        return roomMapper.toRoomResponse(room);
    }
    @PreAuthorize("hasAnyRole('SUPERUSER','LANDLORD')")
    public void deleteObject(String id, String idObject) {
        Room room = this.findById(id);
        Room.ObjectData objectToRemove = null;
        for (Room.ObjectData object : room.getObject()) {
            if (object.getId().toString().equals(idObject)) {
                objectToRemove = object;
                break;
            }
        }
        if (objectToRemove != null) {
            room.getObject().remove(objectToRemove);
        } else {
            throw new AppException(ErrorCode.OBJECT_NOT_FOUND);
        }
        roomRepository.save(room);
    }

}

