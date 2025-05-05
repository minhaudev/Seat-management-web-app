package sourse.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import sourse.entity.TypeSeat;
public interface TypeSeatRepository extends JpaRepository<TypeSeat, String> {
     boolean existsByName(String typeSeatName);
}
