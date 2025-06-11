package bratskov.dev.hotel_view.repositories;


import org.springframework.stereotype.Repository;
import bratskov.dev.hotel_view.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long>,
        JpaSpecificationExecutor<HotelEntity> {
}
