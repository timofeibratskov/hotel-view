package bratskov.dev.hotel_view.entities.embeddeds;

import jakarta.persistence.Embeddable;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Address {
    private String street;
    private Integer houseNumber;
    private String city;
    private String country;
    private String postCode;
}
