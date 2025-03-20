package sourse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import sourse.core.BaseEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Table(name = "users")
public class User extends BaseEntity {
String firstName;
String lastName;
@Column(unique = true, nullable = false)
@Email
String email;
String password;

String phone;
String project;
String team;
@ManyToOne
@JoinColumn(name="room_id", referencedColumnName = "id")
Room room;
@ManyToMany
Set<Role>roles;
}
