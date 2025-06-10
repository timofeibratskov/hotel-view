package bratskov.dev.hotel_view.entities.embeddeds;


import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ArrivalTime {
    @NotBlank(message = "Check-in time must not be blank")
    private String checkIn;
    private String checkOut;
}
