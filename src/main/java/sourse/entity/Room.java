package sourse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.core.BaseEntity;
import sourse.util.JsonConverter;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "rooms")
public class Room extends BaseEntity {
    @Column(unique = true, nullable = false)
    String name;
    String description;
    String nameOwner;
    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    List<ObjectData> object;

    String image;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    User user;

    @ManyToOne
    @JoinColumn(name="hall_id", referencedColumnName = "id")
    Hall hall;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectData {
        UUID id = UUID.randomUUID();
        String name;
        int width;
        int height;
        int ox;
        int oy;
        String color;
    }
}







