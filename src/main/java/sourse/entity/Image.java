package sourse.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import sourse.core.BaseEntity;

import java.time.LocalDateTime;

public class Image  extends BaseEntity {
    String url;
    LocalDateTime uploadAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name="RoomId")
    Room room;
}
