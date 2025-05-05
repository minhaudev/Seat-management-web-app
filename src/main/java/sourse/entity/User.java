package sourse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
String color;
String phone;
@ManyToOne
@JoinColumn(name = "team_id")
Team team;

@ManyToOne
@JsonIgnore
@JoinColumn(name = "project_id")
Project project;

@ManyToOne
@JsonIgnore
@JoinColumn(name="room_id")
Room room;

@ManyToMany
Set<Role>roles;
}
