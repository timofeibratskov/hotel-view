package bratskov.dev.hotel_view.services;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Root;
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
import bratskov.dev.hotel_view.dtos.HotelSearchCriteriaDTO;
import bratskov.dev.hotel_view.repositories.HotelRepository;
import org.springframework.transaction.annotation.Transactional;
import bratskov.dev.hotel_view.exceptions.HotelNotFoundException;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.Optional;
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
            HotelSearchCriteriaDTO searchCriteria) {

        Specification<HotelEntity> spec = (root, query, cb) -> cb.conjunction();

        if (searchCriteria.name() != null && !searchCriteria.name().isEmpty()) {
            spec = spec.and(HotelSpecifications.hasName(searchCriteria.name()));
        }
        if (searchCriteria.brand() != null && !searchCriteria.brand().isEmpty()) {
            spec = spec.and(HotelSpecifications.hasBrand(searchCriteria.brand()));
        }
        if (searchCriteria.city() != null && !searchCriteria.city().isEmpty()) {
            spec = spec.and(HotelSpecifications.hasCity(searchCriteria.city()));
        }
        if (searchCriteria.country() != null && !searchCriteria.country().isEmpty()) {
            spec = spec.and(HotelSpecifications.hasCountry(searchCriteria.country()));
        }
        if (searchCriteria.amenities() != null && !searchCriteria.amenities().isEmpty()) {
            spec = spec.and(HotelSpecifications.hasAmenity(searchCriteria.amenities()));
        }

        List<HotelEntity> hotelEntities = hotelRepository.findAll(spec);
        return hotelEntities.stream()
                .map(mapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HotelFullDto getHotelById(Long id) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() ->
                        new HotelNotFoundException("Hotel not found with id: " + id));
        return mapper.toFullDto(hotelEntity);
    }

    @Transactional
    public HotelShortDto createHotel(CreateHotelRequest request) {
        Optional<HotelEntity> existingHotel = hotelRepository.findOne(
                HotelSpecifications.isUniqueHotel(request.name(), request.address())
        );
        if (existingHotel.isPresent()) {
            throw new IllegalArgumentException("Hotel with name '" + request.name() + "' and address already exists");
        }
        HotelEntity hotelEntity = mapper.toEntity(request);
        hotelRepository.save(hotelEntity);
        return mapper.toShortDto(hotelEntity);
    }

    @Transactional
    public void addAmenities(Long id, List<String> amenities) {
        HotelEntity hotelEntity = hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found with id: " + id));
        Set<String> existingAmenities = hotelEntity.getAmenities();
        Set<String> existingAmenitiesLower = existingAmenities.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        List<String> duplicates = amenities.stream()
                .filter(Objects::nonNull)
                .filter(a -> existingAmenitiesLower.contains(a.toLowerCase()))
                .collect(Collectors.toList());
        if (!duplicates.isEmpty()) {
            throw new IllegalArgumentException(String.format("Amenities %s already exist for this hotel. Please provide a request without them.", duplicates));
        }
        Set<String> newAmenities = amenities.stream()
                .filter(Objects::nonNull)
                .filter(a -> !existingAmenities.contains(a))
                .collect(Collectors.toSet());
        existingAmenities.addAll(newAmenities);
        hotelRepository.save(hotelEntity);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getHistogram(HistogramParam param) {
        if (param == null) {
            throw new IllegalArgumentException("Unsupported histogram param: null");
        }
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
                cq.where(cb.isNotNull(groupByField));
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
                        tuple -> tuple.get(1, Long.class),
                        (a, b) -> a,
                        TreeMap::new
                ));
    }
}
