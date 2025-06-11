package bratskov.dev.hotel_view.dtos;

import bratskov.dev.hotel_view.entities.embeddeds.Address;
import bratskov.dev.hotel_view.entities.embeddeds.Contacts;
import bratskov.dev.hotel_view.entities.embeddeds.ArrivalTime;
import lombok.Builder;

import java.util.Set;

@Builder
public record HotelFullDto(
        Long id,
        String name,
        String description,
        String brand,
        Address address,
        Contacts contacts,
        ArrivalTime arrivalTime,
        Set<String> amenities
) {

}
