package org.test.sotfgen.api.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DailyData(
        // Add fields as needed when you get full data
) {
}
