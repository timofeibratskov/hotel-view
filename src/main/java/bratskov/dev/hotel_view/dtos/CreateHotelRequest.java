package bratskov.dev.hotel_view.dtos;

import lombok.Builder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import bratskov.dev.hotel_view.entities.embeddeds.Address;
import bratskov.dev.hotel_view.entities.embeddeds.Contacts;
import bratskov.dev.hotel_view.entities.embeddeds.ArrivalTime;

@Builder
public record CreateHotelRequest(
        @NotBlank(message = "Name must not be blank")
        @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
        String name,
        @Schema(description = "Hotel description", example = "Luxurious hotel with city views")
        String description,
        @NotBlank(message = "Brand must not be blank")
        @Schema(description = "Hotel brand", example = "Hilton")
        String brand,
        @NotNull(message = "Address must not be null")
        @Valid
        @Schema(description = "Hotel address details")
        Address address,
        @NotNull(message = "Contacts must not be null")
        @Valid
        @Schema(description = "Hotel contact information")
        Contacts contacts,
        @NotNull(message = "Arrival time must not be null")
        @Valid
        @Schema(description = "Hotel arrival and departure times")
        ArrivalTime arrivalTime
) {
}