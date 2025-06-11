package bratskov.dev.hotel_view.dtos;

import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import bratskov.dev.hotel_view.entities.embeddeds.Address;
import bratskov.dev.hotel_view.entities.embeddeds.Contacts;
import bratskov.dev.hotel_view.entities.embeddeds.ArrivalTime;

import java.util.Set;

@Builder
public record HotelFullDto(
        @Schema(description = "Unique hotel ID", example = "1")
        Long id,
        @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
        String name,
        @Schema(description = "Hotel description", example = "Luxurious hotel with city views")
        String description,
        @Schema(description = "Hotel brand", example = "Hilton")
        String brand,
        @Schema(description = "Hotel address details")
        Address address,
        @Schema(description = "Hotel contact information")
        Contacts contacts,
        @Schema(description = "Hotel arrival and departure times")
        ArrivalTime arrivalTime,
        @Schema(description = "Set of hotel amenities", example = "[\"Free WiFi\", \"Pool\"]")
        Set<String> amenities
) {
}