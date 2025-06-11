package bratskov.dev.hotel_view.dtos;

import lombok.Builder;


@Builder
public record HotelShortDto(
        Long id,
        String name,
        String description,
        String address,
        String phone
) {
}
