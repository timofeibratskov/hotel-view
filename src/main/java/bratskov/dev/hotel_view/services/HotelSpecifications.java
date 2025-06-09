package bratskov.dev.hotel_view.services;

import bratskov.dev.hotel_view.entities.HotelEntity;
import org.springframework.data.jpa.domain.Specification;

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

    public static Specification<HotelEntity> hasAmenity(String amenity) {
        return (root, query, cb) ->
                amenity == null ? null : cb.isMember(amenity, root.get("amenities"));
    }
}
