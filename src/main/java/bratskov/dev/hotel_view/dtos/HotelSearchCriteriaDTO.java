package bratskov.dev.hotel_view.dtos;

import java.util.List;

public record HotelSearchCriteriaDTO(
        String name,
        String brand,
        String city,
        String country,
        List<String> amenities
) {
}
