package sourse.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.core.BaseEntity;

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
    
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    User user;

    @ManyToOne
    @JoinColumn(name="hall_id", referencedColumnName = "id")
    Hall hall;
}
