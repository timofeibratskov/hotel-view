package bratskov.dev.hotel_view.enums;

import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
public enum HistogramParam {
    @Schema(name = "brand", description = "Group hotels by brand name")
    BRAND("brand"),
    @Schema(name = "city", description = "Group hotels by city")
    CITY("city"),
    @Schema(name = "country", description = "Group hotels by country")
    COUNTRY("country"),
    @Schema(name = "amenities", description = "Group hotels by available amenities")
    AMENITIES("amenities");

    private final String value;

    HistogramParam(String value) {
        this.value = value;
    }

    public static HistogramParam from(String input) {
        for (HistogramParam p : values()) {
            if (p.value.equalsIgnoreCase(input)) return p;
        }
        throw new IllegalArgumentException("Unknown param: " + input);
    }
}
