package bratskov.dev.hotel_view.repositories;


import bratskov.dev.hotel_view.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, UUID>,
        JpaSpecificationExecutor<HotelEntity> {
}
