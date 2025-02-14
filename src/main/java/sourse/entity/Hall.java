package sourse.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.core.BaseEntity;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "halls")
public class Hall  extends BaseEntity {
    @Column(unique = true, nullable = false)
    String name;
    String description;
    @ManyToOne
    @JoinColumn(name = "floor_id", referencedColumnName = "id")
    Floor floor;

}
