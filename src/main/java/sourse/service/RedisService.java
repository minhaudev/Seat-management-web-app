package sourse.service;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sourse.entity.RoomChange;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addOrUpdateRoomChange(String key, RoomChange newChange) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();

        List<Object> rawList = listOps.range(key, 0, -1);
        List<RoomChange> roomChanges = rawList.stream()
                .map(obj -> objectMapper.convertValue(obj, RoomChange.class))
                .collect(Collectors.toList());


        boolean isUpdated = false;
        for (int i = 0; i < roomChanges.size(); i++) {
            if (roomChanges.get(i).getRoomId().equals(newChange.getRoomId())) {
                roomChanges.set(i, newChange);
                isUpdated = true;
                break;
            }
        }


        if (!isUpdated) {
            roomChanges.add(newChange);
        }

        redisTemplate.delete(key);
        for (RoomChange roomChange : roomChanges) {
            listOps.rightPush(key, roomChange);
        }
    }
    public Optional<RoomChange> getRoomChangeById(String key, String roomId) {
        return getAllRoomChanges(key).stream()
                .filter(change -> change.getRoomId().equals(roomId))
                .findFirst();
    }
    public List<RoomChange> getAllRoomChanges(String key) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List<Object> rawList = listOps.range(key, 0, -1);
        return rawList.stream()
                .map(obj -> objectMapper.convertValue(obj, RoomChange.class))
                .collect(Collectors.toList());
    }
    public void removeRoomChange(String key, String roomId) {
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        List <RoomChange> roomChanges = getAllRoomChanges(key);

        List<RoomChange> updatedList = roomChanges.stream().filter(change -> !change.getRoomId().equals(roomId)).collect(Collectors.toList());
        redisTemplate.delete(key);
        for (RoomChange roomChange : updatedList) {
            listOps.rightPush(key, roomChange);
        }
    }
}
