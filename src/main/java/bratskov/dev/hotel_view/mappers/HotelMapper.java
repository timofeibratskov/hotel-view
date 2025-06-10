package bratskov.dev.hotel_view.mappers;

import org.springframework.stereotype.Component;
import bratskov.dev.hotel_view.dtos.HotelFullDto;
import bratskov.dev.hotel_view.dtos.HotelShortDto;
import bratskov.dev.hotel_view.entities.HotelEntity;
import bratskov.dev.hotel_view.dtos.CreateHotelRequest;

import java.util.UUID;

@Component
public class HotelMapper {
    public HotelEntity toEntity(CreateHotelRequest request) {
        return HotelEntity.builder()
                .id(UUID.randomUUID())
                .name(request.name())
                .description(request.description())
                .address(request.address())
                .brand(request.brand())
                .contacts(request.contacts())
                .arrivalTime(request.arrivalTime())
                .build();
    }

    public HotelShortDto toShortDto(HotelEntity entity) {
        return HotelShortDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .phone(entity.getContacts().getPhone())
                .build();
    }

    public HotelFullDto toFullDto(HotelEntity entity) {
        return HotelFullDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .brand(entity.getBrand())
                .address(entity.getAddress())
                .contacts(entity.getContacts())
                .arrivalTime(entity.getArrivalTime())
                .amenities(entity.getAmenities())
                .build();
    }

}
