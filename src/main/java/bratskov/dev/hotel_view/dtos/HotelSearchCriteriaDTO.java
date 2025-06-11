package bratskov.dev.hotel_view.dtos;

import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@Builder
public record HotelSearchCriteriaDTO(
        @Schema(description = "Hotel name filter", example = "Hilton")
        String name,
        @Schema(description = "Hotel brand filter", example = "Hilton")
        String brand,
        @Schema(description = "City filter", example = "Minsk")
        String city,
        @Schema(description = "Country filter", example = "Belarus")
        String country,
        @Schema(description = "List of amenities filters", example = "[\"Wi-Fi\", \"Pool\"]")
        List<String> amenities
) {
}