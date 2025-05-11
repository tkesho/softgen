package org.test.sotfgen.api.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HourlyData(
        // Add fields as needed when you get full data
) {
}
