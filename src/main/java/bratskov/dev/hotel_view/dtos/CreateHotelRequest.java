package bratskov.dev.hotel_view.dtos;

import lombok.Builder;
import bratskov.dev.hotel_view.entities.embeddeds.Address;
import bratskov.dev.hotel_view.entities.embeddeds.Contacts;
import bratskov.dev.hotel_view.entities.embeddeds.ArrivalTime;

@Builder
public record CreateHotelRequest(
        String name,
        String description,
        String brand,
        Address address,
        Contacts contacts,
        ArrivalTime arrivalTime
) {
}
