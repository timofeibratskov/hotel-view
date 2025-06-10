package bratskov.dev.hotel_view.services;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Expression;
import bratskov.dev.hotel_view.dtos.HotelFullDto;
import jakarta.persistence.criteria.CriteriaQuery;
import bratskov.dev.hotel_view.dtos.HotelShortDto;
import bratskov.dev.hotel_view.mappers.HotelMapper;
import bratskov.dev.hotel_view.enums.HistogramParam;
import jakarta.persistence.criteria.CriteriaBuilder;
import bratskov.dev.hotel_view.entities.HotelEntity;
import bratskov.dev.hotel_view.dtos.CreateHotelRequest;
import org.springframework.data.jpa.domain.Specification;
import bratskov.dev.hotel_view.repositories.HotelRepository;
import bratskov.dev.hotel_view.exceptions.HotelNotFoundException;

import java.util.Map;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper mapper;
    private final EntityManager entityManager;

    public List<HotelShortDto> getAllHotels() {
        List<HotelEntity> hotelEntities = hotelRepository.findAll();
        return hotelEntities.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    public List<HotelShortDto> searchHotels(
            String name, String brand, String city,
            String country, String amenity,
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
        List<HotelEntity> hotelEntities = hotelRepository.findAll(spec, sort);
        return hotelEntities.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    public HotelFullDto getHotelById(UUID id) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new HotelNotFoundException("Hotel not found with id: " + id));
        return mapper.toFullDto(hotelEntity);
    }

    public void createHotel(CreateHotelRequest request) {
        HotelEntity hotelEntity = mapper.toEntity(request);
        hotelRepository.save(hotelEntity);
    }

    public void addAmenities(UUID id, List<String> amenities) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new HotelNotFoundException("Hotel not found with id: " + id));
        hotelEntity.getAmenities().addAll(amenities);
        hotelRepository.save(hotelEntity);
    }

    public Map<String, Long> getHistogram(HistogramParam param) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<HotelEntity> hotelEntityRoot = cq.from(HotelEntity.class);
        Expression<String> groupByField;

        switch (param) {
            case BRAND:
                groupByField = hotelEntityRoot.get("brand");
                cq.where(cb.isNotNull(groupByField));
                break;
            case CITY:
                groupByField = hotelEntityRoot.get("address").get("city");
                cq.where(cb.isNotNull(groupByField));
                break;
            case COUNTRY:
                groupByField = hotelEntityRoot.get("address").get("country");
                cq.where(cb.isNotNull(groupByField));
                break;
            case AMENITIES:
                groupByField = hotelEntityRoot.join("amenities");
                break;
            default:
                throw new IllegalArgumentException("Unsupported histogram param: " + param);

        }

        cq.multiselect(groupByField, cb.count(hotelEntityRoot)).groupBy(groupByField);

        return entityManager.createQuery(cq)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(0, String.class),
                        tuple -> tuple.get(1, Long.class)
                ));
    }
}
