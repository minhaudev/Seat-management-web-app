package sourse.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.entity.Room;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomResponse {
    String id;
    String name;
    String description;
    String owner;
    String nameOwner;
    String image;
    List<Room.ObjectData> object;
    String hallId;
    String floorId;
    String nameHall;
    String nameFloor;
    String created;
}
