package bratskov.dev.hotel_view.services;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import bratskov.dev.hotel_view.entities.HotelEntity;
import org.springframework.data.jpa.domain.Specification;
import bratskov.dev.hotel_view.entities.embeddeds.Address;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HotelSpecifications {
    public static Specification<HotelEntity> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<HotelEntity> hasBrand(String brand) {
        return (root, query, cb) ->
                brand == null ? null : cb.equal(cb.lower(root.get("brand")), brand.toLowerCase());
    }

    public static Specification<HotelEntity> hasCity(String city) {
        return (root, query, cb) ->
                city == null ? null : cb.equal(cb.lower(root.get("address").get("city")), city.toLowerCase());
    }

    public static Specification<HotelEntity> hasCountry(String country) {
        return (root, query, cb) ->
                country == null ? null : cb.equal(cb.lower(root.get("address").get("country")), country.toLowerCase());
    }

    public static Specification<HotelEntity> hasAmenity(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return cb.conjunction();
            }
            List<Predicate> predicates = amenities.stream()
                    .filter(Objects::nonNull)
                    .map(amenity -> {
                        Join<HotelEntity, String> amenityJoin = root.join("amenities");
                        return cb.equal(cb.lower(amenityJoin), cb.lower(cb.literal(amenity)));
                    })
                    .collect(Collectors.toList());
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<HotelEntity> isUniqueHotel(String name, Address address) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("name"), name),
                cb.equal(root.get("address").get("street"), address.getStreet()),
                cb.equal(root.get("address").get("houseNumber"), address.getHouseNumber()),
                cb.equal(root.get("address").get("city"), address.getCity()),
                cb.equal(root.get("address").get("country"), address.getCountry()),
                cb.equal(root.get("address").get("postCode"), address.getPostCode())
        );
    }
}
