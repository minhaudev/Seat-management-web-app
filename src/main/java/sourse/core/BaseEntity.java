package sourse.core;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor()
@AllArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
public abstract class
BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDateTime created = LocalDateTime.now();
    LocalDateTime updated = LocalDateTime.now();
}
