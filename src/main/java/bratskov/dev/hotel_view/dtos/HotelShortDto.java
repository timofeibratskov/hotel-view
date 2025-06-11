package bratskov.dev.hotel_view.dtos;

import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

@Builder
public record HotelShortDto(
        @Schema(description = "Unique hotel ID", example = "1")
        Long id,
        @Schema(description = "Hotel name", example = "DoubleTree by Hilton Minsk")
        String name,
        @Schema(description = "Hotel description", example = "Luxurious hotel with city views")
        String description,
        @Schema(description = "Hotel address", example = "9 Pobediteley Avenue, Minsk, 220004, Belarus")
        String address,
        @Schema(description = "Hotel phone number", example = "+375 17 309-80-00")
        String phone
) {
}