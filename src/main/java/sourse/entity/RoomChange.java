package sourse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "RoomChanges")
public class RoomChange {
     String roomId;
     String  roomName;
     String changedBy;
     List<Room.ObjectData> changedData;
     String status;
}
