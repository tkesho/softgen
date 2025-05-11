package org.test.sotfgen.api.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherData(
        long dt,
        double latitude,
        double longitude,
        String timezone,
        @JsonProperty("timezone_abbreviation") String timezoneAbbreviation,
        @JsonProperty("timezone_offset") int timezoneOffset,
        String units,
        Currently currently,
        Hourly hourly,
        Daily daily
) {
}
