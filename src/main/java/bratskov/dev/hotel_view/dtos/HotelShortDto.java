package bratskov.dev.hotel_view.dtos;

import lombok.Builder;
import bratskov.dev.hotel_view.entities.embeddeds.Address;

import java.util.UUID;

@Builder
public record HotelShortDto(
        UUID id,
        String name,
        String description,
        Address address,
        String phone
) {
}
