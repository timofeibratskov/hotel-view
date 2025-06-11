package bratskov.dev.hotel_view.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import bratskov.dev.hotel_view.dtos.HotelFullDto;
import io.swagger.v3.oas.annotations.media.Schema;
import bratskov.dev.hotel_view.dtos.HotelShortDto;
import io.swagger.v3.oas.annotations.media.Content;
import bratskov.dev.hotel_view.enums.HistogramParam;
import bratskov.dev.hotel_view.services.HotelService;
import bratskov.dev.hotel_view.dtos.CreateHotelRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.annotation.Validated;
import bratskov.dev.hotel_view.dtos.HotelSearchCriteriaDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/property-view")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "Returns a list of hotels based on search criteria such as name, brand, city, country, and amenities.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelShortDto.class)))
    })
    public List<HotelShortDto> searchHotels(
            @Parameter(description = "Hotel name") @RequestParam(required = false) String name,
            @Parameter(description = "Hotel brand") @RequestParam(required = false) String brand,
            @Parameter(description = "City") @RequestParam(required = false) String city,
            @Parameter(description = "Country") @RequestParam(required = false) String country,
            @Parameter(description = "List of amenities", example = "Wi-Fi,Pool") @RequestParam(required = false) List<String> amenities
    ) {
        HotelSearchCriteriaDTO dto = new HotelSearchCriteriaDTO(name, brand, city, country, amenities);
        return hotelService.searchHotels(dto);
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel details", description = "Returns detailed information about a specific hotel by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelFullDto.class))),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public HotelFullDto getHotel(@Parameter(description = "Hotel ID") @PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels", description = "Returns a list of all hotels with short information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelShortDto.class)))
    })
    public List<HotelShortDto> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create a new hotel", description = "Creates a new hotel with the provided details and returns its short information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = HotelShortDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<HotelShortDto> createHotel(@Parameter(description = "Hotel creation request") @RequestBody @Valid CreateHotelRequest request) {
        HotelShortDto dto = hotelService.createHotel(request);
        return ResponseEntity
                .created(URI.create("/property-view/hotels/" + dto.id()))
                .body(dto);
    }

    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities to a hotel", description = "Adds a list of amenities to an existing hotel by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Amenities added successfully"),
            @ApiResponse(responseCode = "404", description = "Hotel not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Void> addAmenities(@Parameter(description = "Hotel ID") @PathVariable @NotNull Long id,
                                             @Parameter(description = "List of amenities to add") @RequestBody @Valid @NotNull @NotEmpty(message = "Amenities list must not be empty")
                                             List<@NotBlank String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram data", description = "Returns the count of hotels grouped by the specified parameter (brand, city, country, or amenities).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"Minsk\": 1, \"Moscow\": 2}"))),
            @ApiResponse(responseCode = "400", description = "Invalid parameter")
    })
    public ResponseEntity<Map<String, Long>> getHistogram(@Parameter(description = "Parameter to group by (brand, city, country, amenities)") @PathVariable @NotNull String param) {
        HistogramParam histogramParam = HistogramParam.from(param);
        return ResponseEntity.ok(hotelService.getHistogram(histogramParam));
    }
}