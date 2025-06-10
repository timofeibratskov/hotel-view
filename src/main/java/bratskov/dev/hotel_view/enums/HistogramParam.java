package bratskov.dev.hotel_view.enums;

import lombok.Getter;

@Getter
public enum HistogramParam {
    BRAND("brand"),
    CITY("city"),
    COUNTRY("country"),
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
