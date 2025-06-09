package bratskov.dev.hotel_view.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;

import java.util.UUID;

@Entity
@Table(name = "hotels")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class HotelEntity {
    @Id
    private UUID id;

    private String name;

    private String description;

    private String brand;

    private String phone;
}



