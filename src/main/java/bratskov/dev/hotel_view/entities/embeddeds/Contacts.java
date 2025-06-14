package bratskov.dev.hotel_view.entities.embeddeds;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
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
public class Contacts {
    @NotBlank(message = "Phone must not be blank")
    @Pattern(regexp = "\\+375\\s\\d{2}\\s\\d{3}-\\d{2}-\\d{2}", message = "Phone must match format +375 XX XXX-XX-XX")
    @Schema(description = "Phone number", example = "+375 17 309-80-00")
    String phone;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Schema(description = "Email address", example = "info@hotel.com")
    String email;
}
