package bratskov.dev.hotel_view.dtos;

import bratskov.dev.hotel_view.entities.embeddeds.Address;
import bratskov.dev.hotel_view.entities.embeddeds.ArrivalTime;
import bratskov.dev.hotel_view.entities.embeddeds.Contacts;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record HotelFullDto(
        UUID id,
        String name,
        String description,
        String brand,
        Address address,
        Contacts contacts,
        ArrivalTime arrivalTime,
        Set<String> amenities
) {

}
