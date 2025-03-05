package sourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.Floor;
import sourse.entity.Hall;

public interface HallRepository extends JpaRepository<Hall, String> {
    boolean existsByName(String hall);
}
