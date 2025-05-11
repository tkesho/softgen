package org.test.sotfgen.api.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherResponseDto(
        String success,
        WeatherData data
) {
}

