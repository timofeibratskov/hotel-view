package bratskov.dev.hotel_view.dtos;

import lombok.Builder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import bratskov.dev.hotel_view.entities.embeddeds.Address;
import bratskov.dev.hotel_view.entities.embeddeds.Contacts;
import bratskov.dev.hotel_view.entities.embeddeds.ArrivalTime;

@Builder
public record CreateHotelRequest(
        @NotBlank(message = "Name must not be blank")
        String name,
        String description,
        @NotBlank(message = "Brand must not be blank")
        String brand,
        @NotNull(message = "Address must not be null")
        @Valid
        Address address,
        @NotNull(message = "Contacts must not be null")
        @Valid
        Contacts contacts,
        @NotNull(message = "Arrival time must not be null")
        @Valid
        ArrivalTime arrivalTime
) {
}
