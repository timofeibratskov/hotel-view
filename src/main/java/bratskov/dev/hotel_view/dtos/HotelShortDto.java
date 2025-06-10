package bratskov.dev.hotel_view.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HotelShortDto(
        UUID id,
        String name,
        String description,
        String address,
        String phone
) {
}
