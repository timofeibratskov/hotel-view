package bratskov.dev.hotel_view.controllers;

import lombok.RequiredArgsConstructor;
import bratskov.dev.hotel_view.entities.HotelEntity;
import bratskov.dev.hotel_view.services.HotelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/property-view")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/search")
    public List<HotelEntity> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenity,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        return hotelService.getAllHotels(name, brand, city, country, amenity, sortBy, direction);
    }

    @PostMapping("/hotels")
    public void getAll(@RequestBody HotelEntity entity) {
        hotelService.createHotel(entity);
    }

    @GetMapping("/hotels/{id}")
    public HotelEntity getHotel(@PathVariable UUID id) {
        return hotelService.getHotelById(id);
    }

}
