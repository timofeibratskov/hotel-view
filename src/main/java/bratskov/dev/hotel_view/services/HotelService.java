package bratskov.dev.hotel_view.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import bratskov.dev.hotel_view.entities.HotelEntity;
import org.springframework.data.jpa.domain.Specification;
import bratskov.dev.hotel_view.repositories.HotelRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;

    public List<HotelEntity> getAllHotels(String name, String brand, String city, String country, String amenity,
                                          String sortBy, String direction) {

        Specification<HotelEntity> spec = (root, query, cb) -> cb.conjunction();

        if (name != null) {
            spec = spec.and(HotelSpecifications.hasName(name));
        }
        if (brand != null) {
            spec = spec.and(HotelSpecifications.hasBrand(brand));
        }
        if (city != null) {
            spec = spec.and(HotelSpecifications.hasCity(city));
        }
        if (country != null) {
            spec = spec.and(HotelSpecifications.hasCountry(country));
        }
        if (amenity != null) {
            spec = spec.and(HotelSpecifications.hasAmenity(amenity));
        }

        Sort.Direction dir = Sort.Direction.fromString(direction == null ? "asc" : direction);
        String sortField = sortBy == null ? "name" : sortBy;
        Sort sort = Sort.by(dir, sortField);

        return hotelRepository.findAll(spec, sort);
    }


    public HotelEntity getHotelById(UUID id) {
        return hotelRepository.findById(id).orElseThrow();
    }

    public void createHotel(HotelEntity entity) {
        hotelRepository.save(entity);
    }
}
