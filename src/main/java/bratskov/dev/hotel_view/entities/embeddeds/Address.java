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

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Address {
    @NotBlank(message = "Street must not be blank")
    private String street;
    @NotNull(message = "House number must not be null")
    private Integer houseNumber;
    @NotBlank(message = "City must not be blank")
    private String city;
    @NotBlank(message = "Country must not be blank")
    private String country;
    @NotBlank(message = "Post code must not be blank")
    private String postCode;
}
