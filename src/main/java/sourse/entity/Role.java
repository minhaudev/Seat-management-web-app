package sourse.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import sourse.core.BaseEntity;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
public class Role{
    @Id
    @Column(unique = true, nullable = false)
    String name;
    String description;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Permission> permissions;
}
