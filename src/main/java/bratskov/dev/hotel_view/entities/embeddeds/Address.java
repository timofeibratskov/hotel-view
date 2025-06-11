package bratskov.dev.hotel_view.entities.embeddeds;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Address {
    @NotBlank(message = "Street must not be blank")
    @Schema(description = "Street name", example = "Pobediteley Avenue")
    String street;
    @NotNull(message = "House number must not be null")
    @Schema(description = "House number", example = "9")
    Integer houseNumber;
    @NotBlank(message = "City must not be blank")
    @Schema(description = "City name", example = "Minsk")
    String city;
    @NotBlank(message = "Country must not be blank")
    @Schema(description = "Country name", example = "Belarus")
    String country;
    @NotBlank(message = "Post code must not be blank")
    @Schema(description = "Postal code", example = "220004")
    String postCode;
}
