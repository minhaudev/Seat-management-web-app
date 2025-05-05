package sourse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.dto.request.SeatUpdatePositionRequest;
import sourse.dto.response.SeatResponse;

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
     List<SeatResponse> seatData;
     String status;
}
