package bratskov.dev.hotel_view.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import bratskov.dev.hotel_view.dtos.HotelFullDto;
import bratskov.dev.hotel_view.dtos.HotelShortDto;
import bratskov.dev.hotel_view.enums.HistogramParam;
import bratskov.dev.hotel_view.services.HotelService;
import bratskov.dev.hotel_view.dtos.CreateHotelRequest;
import org.springframework.web.bind.annotation.GetMapping;
import bratskov.dev.hotel_view.dtos.HotelSearchCriteriaDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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
            @RequestParam(required = false) List<String> amenities
    ) {
        HotelSearchCriteriaDTO dto = new HotelSearchCriteriaDTO(name, brand, city, country, amenities);
        return hotelService.searchHotels(dto);
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
    public ResponseEntity<HotelShortDto> createHotel(@RequestBody @Valid CreateHotelRequest request) {
        HotelShortDto dto = hotelService.createHotel(request);
        return ResponseEntity
                .created(URI.create("/property-view/hotels/" + dto.id()))
                .body(dto);
    }

    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenities(@PathVariable @NotNull UUID id,
                                             @RequestBody @Valid @NotNull @NotEmpty(message = "Amenities list must not be empty")
                                             List<@NotBlank String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable @NotNull String param) {
        HistogramParam histogramParam = HistogramParam.from(param);
        return ResponseEntity.ok(hotelService.getHistogram(histogramParam));
    }
}
