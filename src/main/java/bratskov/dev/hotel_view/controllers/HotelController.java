package bratskov.dev.hotel_view.controllers;

import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import bratskov.dev.hotel_view.dtos.HotelFullDto;
import bratskov.dev.hotel_view.dtos.HotelShortDto;
import bratskov.dev.hotel_view.enums.HistogramParam;
import bratskov.dev.hotel_view.services.HotelService;
import bratskov.dev.hotel_view.dtos.CreateHotelRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/property-view")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/search")
    public List<HotelShortDto> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenity,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        return hotelService.searchHotels(name, brand, city, country, amenity, sortBy, direction);
    }

    @GetMapping("/hotels/{id}")
    public HotelFullDto getHotel(@PathVariable UUID id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping("/hotels")
    public List<HotelShortDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @PostMapping("/hotels")
    public void createHotel(@RequestBody CreateHotelRequest request) {
        hotelService.createHotel(request);
    }

    @PostMapping("/hotels/{id}/amenities")
    public void addAmenities(@PathVariable UUID id,
                             @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable @NotNull String param) {
        HistogramParam histogramParam = HistogramParam.from(param);
        return ResponseEntity.ok(hotelService.getHistogram(histogramParam));
    }
}
