package bratskov.dev.hotel_view.entities.embeddeds;


import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArrivalTime {
    @NotBlank(message = "Check-in time must not be blank")
    @Schema(description = "Check-in time", example = "14:00")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Check-in time must be in HH:MM format (e.g., 14:00)")
    String checkIn;
    @Schema(description = "Check-out time", example = "12:00")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Check-in time must be in HH:MM format (e.g., 14:00)")
    String checkOut;
}
