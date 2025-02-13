package sourse.entity;

import jakarta.persistence.Table;
import sourse.core.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User extends BaseEntity {
String firstName;
String lastName;
String email;
String password;
String phone;
String roleId;
String projectId;
String teamId;
}
