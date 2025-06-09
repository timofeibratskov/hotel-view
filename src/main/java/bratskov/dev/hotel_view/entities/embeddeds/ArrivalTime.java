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
public class ArrivalTime {
    private String checkIn;
    private String checkOut;
}
